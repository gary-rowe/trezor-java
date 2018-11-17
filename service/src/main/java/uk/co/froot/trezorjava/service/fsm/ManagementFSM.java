package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;

/**
 * A finite state machine (FSM) to handle Trezor device management use cases.
 *
 * Uses an asynchronous approach so that a state change triggers an event
 * via a device message which responds at a later time to complete the transition.
 */
public class ManagementFSM implements TrezorEventListener {

  private ManagementState currentState = new DetachedState();
  private final TrezorDeviceManager deviceManager;

  /**
   *
    * @param deviceManager The Trezor device manager providing hardware and UI state information.
   */
  public ManagementFSM(TrezorDeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }

  /**
   * Start the transition from one state to another within the context of an overall use case.
   *
   * @param newState The target state.
   */
  public void transitionTo(ManagementState newState) {

    // Exit the previous state, update and enter the new one
    currentState.exit(deviceManager);
    currentState = newState;
    currentState.enter(deviceManager);

  }

  @Override
  public void onTrezorEvent(TrezorEvent event) {

    // TODO Indicate the transition has finished

  }
}
