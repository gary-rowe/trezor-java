package uk.co.froot.trezorjava.core.events;

import com.google.protobuf.Message;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;

public class TrezorEvent {

  private final Message message;
  private final TrezorDeviceManager deviceManagaer;

  public TrezorEvent(TrezorDeviceManager deviceManager, Message message) {
    this.deviceManagaer = deviceManager;
    this.message = message;
  }

  /**
   * @return The Trezor device manager giving access to more detailed information.
   */
  public TrezorDeviceManager getDeviceManager() {
    return deviceManagaer;
  }

  /**
   * @return The low level message triggering this event. If noll then the device connection state has changed.
   */
  public Message getMessage() {
    return message;
  }

}
