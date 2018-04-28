package uk.co.froot.trezorjava;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.BufferUtils;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;
import uk.co.froot.trezorjava.utils.ConsoleUtils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static uk.co.froot.trezorjava.utils.ConsoleUtils.formatBytesAsHex;

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
  public Message sendMessage(Message message) {
    if (deviceHandle == null)
      throw new IllegalStateException("sendMessage: usbConnection already closed, cannot send message");

    messageWrite(message);
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

    // Only perform byte presentation if debug is enabled
    if (log.isDebugEnabled()) {

      buffer.rewind();
      byte[] writeBytes = new byte[buffer.remaining()];
      buffer.get(writeBytes);
      log.debug("\n>{}", formatBytesAsHex(writeBytes));
    }

    log.debug("> {} ({} message bytes, padded to {} bytes, sending as {} chunks)", msgName, msgSize, bufferSize, chunkCount);

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
      log.debug("> {} bytes sent to device.", transferred.get());
    }


  }

  private Message messageRead() {

    ByteBuffer data = BufferUtils
      .allocateByteBuffer(64)
      .order(ByteOrder.LITTLE_ENDIAN);

    IntBuffer transferred = BufferUtils.allocateIntBuffer();

    int result = LibUsb.bulkTransfer(
      deviceHandle,
      IN_ENDPOINT,
      data,
      transferred,
      TIMEOUT
    );
    if (result != LibUsb.SUCCESS) {
      throw new LibUsbException("Unable to read data", result);
    }
    log.debug("< {} bytes read from device.", transferred.get());

    // Only perform byte presentation if debug is enabled
    if (log.isDebugEnabled()) {
      byte[] readBytes = new byte[data.remaining()];
      data.get(readBytes);
      log.debug("\n>{}", ConsoleUtils.formatBytesAsHex(readBytes));
    }

    // TODO Perform

    // TODO Decode into a protobuf message

    return null;
  }

  /**
   * Parse a message from the raw bytes from the device.
   *
   * @param type The message type
   * @param data The raw bytes
   *
   * @return The decoded protobuf message.
   *
   * @throws InvalidProtocolBufferException If something goes wrong.
   */
  public Message parseMessageFromBytes(TrezorMessage.MessageType type, byte[] data) throws InvalidProtocolBufferException {
    try {
      String className = TrezorMessage.class.getName() + "$" + type.name().replace("MessageType_", "");
      Class cls = Class.forName(className);
      Method method = cls.getDeclaredMethod("parseFrom", byte[].class);
      //noinspection PrimitiveArrayArgumentToVariableArgMethod
      return (Message) method.invoke(null, data); // TODO cache methods for faster start?
    } catch (Exception ex) {
      throw new InvalidProtocolBufferException("Exception while calling: parseMessageFromBytes for MessageType: " + type.name());
    }

  }


}

