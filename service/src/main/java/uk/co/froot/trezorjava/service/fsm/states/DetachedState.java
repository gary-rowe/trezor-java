package uk.co.froot.trezorjava.service.fsm.states;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * The device has been detached and low level communication has ceased.
 */
public class DetachedState extends AbstractManagementState {
  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {
    // Do nothing
  }

  @Override
  public void doExit(TrezorDeviceManager deviceManager) {
    // Do nothing
  }

  @Override
  public ManagementState lookupStateByEvent(TrezorEvent event) {

    // TODO Determine appropriate events
    return this;
  }
}
