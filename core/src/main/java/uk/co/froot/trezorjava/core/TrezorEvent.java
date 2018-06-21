package uk.co.froot.trezorjava.core;

import javax.usb.UsbDeviceDescriptor;

public class TrezorEvent {

  private final TrezorType trezorType;
  private final UsbDeviceDescriptor usbDeviceDescriptor;
  private final TrezorEventType eventType;


  public TrezorEvent(TrezorEventType eventType,
                     TrezorType trezorType,
                     UsbDeviceDescriptor usbDeviceDescriptor
  ) {
    this.trezorType = trezorType;
    this.usbDeviceDescriptor = usbDeviceDescriptor;
    this.eventType = eventType;
  }

  /**
   * @return The Trezor type.
   */
  public TrezorType getTrezorType() {
    return trezorType;
  }

  /**
   * @return The ow level USB descriptor for more information on the device.
   */
  public UsbDeviceDescriptor getUsbDeviceDescriptor() {
    return usbDeviceDescriptor;
  }

  /**
   *
   * @return The Trezor event type to allow the UI to present appropriate information to the user.
   */
  public TrezorEventType getEventType() {
    return eventType;
  }
}
