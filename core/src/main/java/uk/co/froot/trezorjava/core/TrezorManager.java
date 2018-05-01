package uk.co.froot.trezorjava.core;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;
import uk.co.froot.trezorjava.core.internal.TrezorDevice;

/**
 * Manager class to provide the following:
 *
 * Access to the top level Trezor API.
 */
public class TrezorManager {

  private static final Logger log = LoggerFactory.getLogger(TrezorManager.class);

  private static final int TREZOR_INTERFACE = 0;

  /**
   * The Trezor device.
   */
  private TrezorDevice trezorDevice = null;

  /**
   * TrezorManager method.
   *
   * @throws LibUsbException If something goes wrong.
   */
  public void initialise() throws LibUsbException {

    initLibUsb();

    tryGetDevice();

  }

  /**
   * Send a message to the device.
   *
   * This is a blocking call.
   *
   * @param message A protobuf message to send to the device.
   *
   * @return A response message from the device.
   *
   * @throws InvalidProtocolBufferException If something goes wrong.
   */
  public Message sendMessage(Message message) throws InvalidProtocolBufferException {
    return trezorDevice.sendMessage(message);
  }

  /**
   * Initialise libusb context.
   */
  private static void initLibUsb() {

    log.debug("Initialising libusb...");

    int result = LibUsb.init(null);
    if (result != LibUsb.SUCCESS) {
      throw new LibUsbException("Unable to initialize libusb.", result);
    }

  }

  /**
   * @param descriptor The USB device descriptor.
   *
   * @return True if this is a recognised Trezor device.
   */
  private boolean isTrezorDevice(DeviceDescriptor descriptor) {

    if (descriptor.idVendor() == 0x534c && descriptor.idProduct() == 0x0001) {
      log.debug("Found Trezor V1");
      return true;
    }

    // TREZOR v2
    if (descriptor.idVendor() == 0x1209 && descriptor.idProduct() == 0x53c0 || descriptor.idProduct() == 0x53c1) {
      log.debug("Found Trezor V2");
      return true;
    }

    // Must have failed to be here
    return false;
  }

  private synchronized boolean tryConnectDevice() {
    return tryGetDevice() != null;
  }

  /**
   * Try to get a Trezor device.
   *
   * @return A USB device if a Trezor is detected or null.
   */
  private TrezorDevice tryGetDevice() {

    if (trezorDevice == null) {
      log.debug("Finding Trezor in device list...");

      // Open the device
      DeviceHandle handle = LibUsb.openDeviceWithVidPid(
        null,
        (short) 0x1209,
        (short) 0x53c1
      );
      if (handle == null) {
        log.error("Test device not found.");
        return null;
      }

      // Claim interface
      int result = LibUsb.claimInterface(handle, TREZOR_INTERFACE);
      if (result != LibUsb.SUCCESS) {
        throw new LibUsbException("Unable to claim interface", result);
      }

      // Must have a Trezor device to be here
      log.info("Trezor device interfaces verified.");
      trezorDevice = new TrezorDevice(handle);

      return trezorDevice;

    } else {
      log.info("Using already connected device.");
      return trezorDevice;

    }

  }


}
