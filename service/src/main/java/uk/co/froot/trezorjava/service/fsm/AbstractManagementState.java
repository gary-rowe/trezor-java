package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * Abstract base class to support management messages and state progressions
 */
public abstract class AbstractManagementState implements ManagementState {
  @Override
  public void enter(TrezorDeviceManager deviceManager) {
    // Check for common device state

    // Hand over to state-specific handling
    doEnter(deviceManager);
  }

  /**
   * Internal handling of state-specific messages upon entry to a new state.
   *
   * @param deviceManager The device manager.
   */
  public abstract void doEnter(TrezorDeviceManager deviceManager);

  @Override
  public void exit(TrezorDeviceManager deviceManager) {
    // Perform common device state updates

    doExit(deviceManager);

  }

  /**
   * Internal handling of state-specific messages upon exit from an old state.
   *
   * @param deviceManager The device manager.
   */
  public abstract void doExit(TrezorDeviceManager deviceManager);

}
