package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

public interface ManagementState {

  /**
   * Called when entering a new state.
   *
   * @param deviceManager The device manager providing access to low level state information.
   */
  void enter(TrezorDeviceManager deviceManager);

  /**
   * Called when exiting an old state.
   *
   * @param deviceManager The device manager providing access to low level state information.
   */
  void exit(TrezorDeviceManager deviceManager);
}
