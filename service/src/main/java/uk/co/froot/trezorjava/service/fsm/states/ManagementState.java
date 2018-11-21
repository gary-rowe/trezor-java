package uk.co.froot.trezorjava.service.fsm.states;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

public interface ManagementState {

  /**
   * Called when entering a new state.
   * Typically triggers a message to the device.
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

  /**
   * Called when a Trezor event has occurred in the CONNECTED device state.
   *
   * @param event The Trezor event.
   * @return The appropriate next state for the given event.
   */
  ManagementState lookupStateByEvent(TrezorEvent event);
}
