package uk.co.froot.trezorjava.core;

import javax.usb.UsbDeviceDescriptor;

/**
 * Downstream API consumers should implement this to respond to events coming from the device.
 */
public interface TrezorEventHandler {

  /**
   * Called when a Trezor device is attached.
   *
   * @param trezorType The Trezor type.
   * @param descriptor The USB device descriptor providing more detail.
   */
  void handleDeviceAttached(TrezorType trezorType, UsbDeviceDescriptor descriptor);

  /**
   * Called when a Trezor device is detached.
   *
   * @param trezorType The Trezor type.
   * @param descriptor The USB device descriptor providing more detail.
   */
  void handleDeviceDetached(TrezorType trezorType, UsbDeviceDescriptor descriptor);

}
