package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * A finite state machine (FSM) to handle Trezor device management use cases.
 */
public class ManagementFSM  {

  private ManagementState currentState;
  private final TrezorDeviceManager deviceManager;

  /**
   *
    * @param deviceManager The Trezor device manager providing hardware and UI state information.
   */
  public ManagementFSM(TrezorDeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }

  public void transitionTo(ManagementState newState) {

    currentState = newState;
    currentState.enter(deviceManager);

  }

}
