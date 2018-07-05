package uk.co.froot.trezorjava.core.events;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import uk.co.froot.trezorjava.core.TrezorDeviceState;
import uk.co.froot.trezorjava.core.TrezorType;
import uk.co.froot.trezorjava.core.TrezorUIState;

import javax.usb.UsbDeviceDescriptor;

public class TrezorEvent {

  private final TrezorUIState uiState;
  private final TrezorDeviceState deviceState;
  private final TrezorType trezorType;
  private final UsbDeviceDescriptor usbDeviceDescriptor;
  private TrezorMessage trezorMessage = null;

  public TrezorEvent(TrezorDeviceState deviceState,
                     TrezorUIState uiState,
                     TrezorType trezorType,
                     UsbDeviceDescriptor usbDeviceDescriptor,
                     TrezorMessage trezorMessage) {
    this.deviceState = deviceState;
    this.trezorType = trezorType;
    this.usbDeviceDescriptor = usbDeviceDescriptor;
    this.uiState = uiState;
    this.trezorMessage = trezorMessage;
  }

  /**
   * @return The Trezor UI state when this event was generated.
   */
  public TrezorUIState getUIState() {
    return uiState;
  }

  /**
   * @return The Trezor device state when this event was generated.
   */
  public TrezorDeviceState getDeviceState() { return deviceState; }

  /**
   * @return The type of Trezor device generating this event.
   */
  public TrezorType getTrezorType() {
    return trezorType;
  }

  /**
   * @return The low level Trezor message triggering this event.
   */
  public TrezorMessage getTrezorMessasge() {
    return trezorMessage;
  }

  /**
   * @return The low level USB descriptor for more information on the device.
   */
  public UsbDeviceDescriptor getUsbDeviceDescriptor() {
    return usbDeviceDescriptor;
  }

}
