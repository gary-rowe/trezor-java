package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

public interface ManagementState {

  /**
   * Called when entering a new state.F
   * @param deviceManager The device manager providing access to low level state information.
   */
  void enter(TrezorDeviceManager deviceManager);
}
