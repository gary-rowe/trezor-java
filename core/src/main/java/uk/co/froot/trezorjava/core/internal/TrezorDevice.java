package uk.co.froot.trezorjava.core.internal;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.BufferUtils;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

import static uk.co.froot.trezorjava.core.utils.ConsoleUtils.formatBytesAsHex;

/**
 * Internal class to represent a Trezor device for byte-level message passing.
 *
 * @see uk.co.froot.trezorjava.core.TrezorDeviceManager TrezorDeviceManager for external API.
 * @since 0.0.1
 */
public class TrezorDevice {

  private static final Logger log = LoggerFactory.getLogger(TrezorDevice.class);

  /**
   * The input endpoint (device to host).
   */
  private static final byte IN_ENDPOINT = (byte) 0x81;

  /**
   * The output endpoint (host to device).
   */
  private static final byte OUT_ENDPOINT = (byte) 0x01;

  /**
   * USB communication timeout in milliseconds.
   */
  private static final int TIMEOUT = 3000;

  /**
   * USB device handle.
   */
  private final DeviceHandle deviceHandle;

  /**
   * @param deviceHandle The device handle.
   */
  public TrezorDevice(DeviceHandle deviceHandle) {
    this.deviceHandle = deviceHandle;
  }

  /**
   * Send a message to the device.
   *
   * @param message The protobuf message to send.
   *
   * @return The decoded protobuf message given in response.
   */
  public Message sendMessage(Message message) throws InvalidProtocolBufferException {
    if (deviceHandle == null)
      throw new IllegalStateException("sendMessage: usbConnection already closed, cannot send message");

    // Write the message
    messageWrite(message);

    // Immediately block for a response
    return messageRead();
  }

  /**
   * Close this device.
   */
  public void close() {

    if (this.deviceHandle != null) {
      LibUsb.close(deviceHandle);
    }
  }

  /**
   * Write a message to the Trezor including a suitable header inferred from the protobuf structure.
   *
   * @param message The protobuf message to write to the USB device.
   */
  private void messageWrite(Message message) {

    // Message parameters
    int msgSize = message.getSerializedSize();
    String msgName = message.getClass().getSimpleName();
    int msgId = TrezorMessage.MessageType.valueOf("MessageType_" + msgName).getNumber();

    // Buffer parameters
    int bufferSize = ((msgSize + 9 + 64) / 64) * 64;
    int chunkCount = bufferSize / 64;

    // Maximum size of a message buffer after padding
    ByteBuffer buffer = BufferUtils.allocateByteBuffer(bufferSize);

    // Build the Trezor message header
    buffer.put((byte) '?'); // Length of chunk
    buffer.put((byte) '#'); // Sync 1
    buffer.put((byte) '#'); // Sync 2
    buffer.put((byte) ((msgId >> 8) & 0xFF)); // Message Id
    buffer.put((byte) (msgId & 0xFF));
    buffer.put((byte) ((msgSize >> 24) & 0xFF)); // Message size (little endian)
    buffer.put((byte) ((msgSize >> 16) & 0xFF));
    buffer.put((byte) ((msgSize >> 8) & 0xFF));
    buffer.put((byte) (msgSize & 0xFF));

    // Add the message payload
    buffer.put(message.toByteArray());

    // Pad out to 64 byte boundary
    while (buffer.position() % 63 > 0) {
      buffer.put((byte) 0);
    }

    log.debug("> {} ({} message bytes, padded to {} bytes, sending as {} chunks)", msgName, msgSize, bufferSize, chunkCount);

    // Only perform byte presentation if debug is enabled
    if (log.isDebugEnabled()) {

      buffer.rewind();
      byte[] writeBytes = new byte[buffer.remaining()];
      buffer.get(writeBytes);
      log.debug("\n> Payload{}", formatBytesAsHex(writeBytes, true));
    }

    // Break the overall message into 64 byte chunks
    buffer.rewind();
    for (int chunk = 0; chunk < chunkCount; chunk++) {
      // Get 64 byte chunk from message and wrap in ByteBuffer
      byte[] chunkBytes = new byte[64];
      buffer.get(chunkBytes, chunk * 64, 64);
      ByteBuffer chunkBuffer = BufferUtils.allocateByteBuffer(64);
      chunkBuffer.put(chunkBytes);
      IntBuffer transferred = BufferUtils.allocateIntBuffer();

      // Send to device
      int result = LibUsb.bulkTransfer(
        deviceHandle,
        OUT_ENDPOINT,
        chunkBuffer,
        transferred,
        TIMEOUT
      );
      // Error checking
      if (result != LibUsb.SUCCESS) {
        throw new LibUsbException("Unable to send data", result);
      }
    }

  }

  /**
   * Read a message from the device.
   *
   * @return A protobuf message from the device.
   *
   * @throws InvalidProtocolBufferException If something goes wrong.
   */
  @SuppressWarnings("unchecked")
  private Message messageRead() throws InvalidProtocolBufferException {

    ByteBuffer messageBuffer;
    TrezorMessage.MessageType messageType;
    int invalidChunksCounter = 0;

    int msgId;
    int msgSize;

    // Start by attempting to read the first chunk
    // with the assumption that the read buffer initially
    // contains random data and needs to be synch'd up
    for (; ; ) {
      ByteBuffer chunkBuffer = BufferUtils
        .allocateByteBuffer(64)
        .order(ByteOrder.LITTLE_ENDIAN);
      IntBuffer transferred = BufferUtils.allocateIntBuffer();

      int result = LibUsb.bulkTransfer(
        deviceHandle,
        IN_ENDPOINT,
        chunkBuffer,
        transferred,
        TIMEOUT
      );
      if (result != LibUsb.SUCCESS) {
        throw new LibUsbException("Unable to read data", result);
      }

      // Extract the chunk
      byte[] readBytes = new byte[chunkBuffer.remaining()];
      chunkBuffer.get(readBytes);

      // Check for invalid header length
      if (readBytes.length < 9) {
        if (invalidChunksCounter++ > 5) {
          log.debug("\n< Header{}", ConsoleUtils.formatBytesAsHex(readBytes, true));
          throw new InvalidProtocolBufferException("Header too short after multiple chunks");
        }
        // Restart the loop
        continue;
      }

      // Check for invalid header sync pattern
      if (readBytes[0] != (byte) '?'
        || readBytes[1] != (byte) '#'
        || readBytes[2] != (byte) '#') {
        if (invalidChunksCounter++ > 5) {
          log.debug("\n< Header{}", ConsoleUtils.formatBytesAsHex(readBytes, true));
          throw new InvalidProtocolBufferException("Header invalid after multiple chunks");
        }
        // Restart the loop
        continue;
      }

      // Must be OK to be here
      log.debug("\n< Header{}", ConsoleUtils.formatBytesAsHex(readBytes, true));

      msgId = (((int) readBytes[3] & 0xFF) << 8) + ((int) readBytes[4] & 0xFF);
      msgSize = (((int) readBytes[5] & 0xFF) << 24)
        + (((int) readBytes[6] & 0xFF) << 16)
        + (((int) readBytes[7] & 0xFF) << 8)
        + ((int) readBytes[8] & 0xFF);

      // Allocate the message payload buffer
      messageBuffer = ByteBuffer.allocate(msgSize + 1024);
      messageBuffer.put(readBytes, 9, readBytes.length - 9);
      messageType = TrezorMessage.MessageType.forNumber(msgId);
      break;
    }

    // Read in the remaining payload data
    invalidChunksCounter = 0;

    while (messageBuffer.position() < msgSize) {
      ByteBuffer chunkBuffer = BufferUtils
        .allocateByteBuffer(64)
        .order(ByteOrder.LITTLE_ENDIAN);
      IntBuffer transferred = BufferUtils.allocateIntBuffer();

      int result = LibUsb.bulkTransfer(
        deviceHandle,
        IN_ENDPOINT,
        chunkBuffer,
        transferred,
        TIMEOUT
      );
      if (result != LibUsb.SUCCESS) {
        throw new LibUsbException("Unable to read data", result);
      }

      // Extract the chunk
      byte[] readBytes = new byte[chunkBuffer.remaining()];
      chunkBuffer.get(readBytes);

      // Sanity check on the chunk
      if (readBytes[0] != (byte) '?') {
        // Unexpected value in the first position - should be 63
        if (invalidChunksCounter++ > 5)
          throw new InvalidProtocolBufferException("Chunk invalid in payload");
        continue;
      }
      messageBuffer.put(readBytes, 1, readBytes.length - 1);
    }

    byte[] msgData = Arrays.copyOfRange(messageBuffer.array(), 0, msgSize);

    log.debug("\n< Message{}", ConsoleUtils.formatBytesAsHex(msgData, true));

    log.info("Parsing type {} ({} bytes):", messageType, msgData.length);
    try {
      Method method = extractParserMethod(messageType);
      //noinspection PrimitiveArrayArgumentToVariableArgMethod
      return (Message) method.invoke(null, msgData);
    } catch (Exception ex) {
      throw new InvalidProtocolBufferException("Exception while calling: parseMessageFromBytes for MessageType: " + messageType.name());
    }

  }

  /**
   *
   * @param messageType The abstract message type
   * @return
   * @throws ClassNotFoundException If
   * @throws NoSuchMethodException
   */
  private Method extractParserMethod(TrezorMessage.MessageType messageType) throws ClassNotFoundException, NoSuchMethodException {

    // Identify the expected inner class name
    String innerClassName = messageType.name().replace("MessageType_", "");

    // Identify the default class name
    String className = TrezorMessageManagement.class.getName() + "$" + innerClassName;

    // Search for any known sub-groups
    if (innerClassName.startsWith("Ethereum")) {
      className = TrezorMessageEthereum.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("NEM")) {
      className = TrezorMessageNem.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Lisk")) {
      className = TrezorMessageLisk.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Tezos")) {
      className = TrezorMessageTezos.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Stellar")) {
      className = TrezorMessageStellar.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Tron")) {
      className = TrezorMessageTron.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Cardano")) {
      className = TrezorMessageCardano.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Ontology")) {
      className = TrezorMessageOntology.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Ripple")) {
      className = TrezorMessageRipple.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("Monero")) {
      className = TrezorMessageMonero.class.getName() + "$" + innerClassName;
    }
    if (innerClassName.startsWith("DebugMonero")) {
      className = TrezorMessageMonero.class.getName() + "$" + innerClassName;
    }

    log.debug("Class name: {}", className);
    Class cls = Class.forName(className);
    return cls.getDeclaredMethod("parseFrom", byte[].class);
  }

}

