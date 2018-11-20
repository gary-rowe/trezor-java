package uk.co.froot.trezorjava.service.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.core.events.TrezorEvents;

/**
 * A finite state machine (FSM) to handle Trezor device management use cases.
 *
 * Uses an asynchronous approach so that a state change triggers an event
 * via a device message which responds at a later time to complete the transition.
 */
public class ManagementFSM implements TrezorEventListener {

  private static final Logger log = LoggerFactory.getLogger(ManagementFSM.class);

  private ManagementState currentState = new DetachedState();
  private final TrezorDeviceManager deviceManager;

  /**
   *
    * @param deviceManager The Trezor device manager providing hardware and UI state information.
   */
  public ManagementFSM(TrezorDeviceManager deviceManager) {
    this.deviceManager = deviceManager;
    TrezorEvents.register(this);
  }

  /**
   * Start the transition from one state to another within the context of an overall use case.
   *
   * @param newState The target state.
   */
  public void transitionTo(ManagementState newState) {

    // Check if we are returning to the same state to avoid loops
    if (newState.getClass().getName().equals(currentState.getClass().getName())) {
      // Do nothing
      return;
    }

    // Exit the previous state, update and enter the new one.
    currentState.exit(deviceManager);
    currentState = newState;
    currentState.enter(deviceManager);

  }

  @Override
  public void onTrezorEvent(TrezorEvent event) {

    // Ensure this event is for us.
    if (event.getDeviceManager() != deviceManager) {
      // Ignore
      return;
    }

    // Check for common failure messages
    switch (event.getDeviceManager().context().getDeviceState()) {
      case DEVICE_FAILED:
        // Treat as detached.
        transitionTo(new DetachedState());
        break;
      case DEVICE_DETACHED:
        // Can simply wait for another device to be connected again
        transitionTo(new DetachedState());
        break;
      case DEVICE_ATTACHED:
        // Do nothing and return
        return;
      case DEVICE_CONNECTED:
        // Do nothing and proceed
        break;
      default:
        // Failure - report and return
        log.warn("Unexpected message from device.");
        return;
    }

    // Must be connected to be here so use the current state to
    // determine the next one based on the event
    transitionTo(currentState.lookupStateByEvent(event));

  }
}
