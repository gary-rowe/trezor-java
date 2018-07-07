package uk.co.froot.trezorjava.core;

import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * Describes the behaviour of a state transition within a Trezor device.
 */
public interface TrezorState {

  /**
   * Action to perform when entering this state.
   *
   * @param event The Trezor event triggering the entry.
   */
  void onEntry(TrezorEvent event);

  /**
   * Action to perform when exiting this state.
   *
   * @param event The Trezor event triggering the exit.
   */
  void onExit(TrezorEvent event);

}
