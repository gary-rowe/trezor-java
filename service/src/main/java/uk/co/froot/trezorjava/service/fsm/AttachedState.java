package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * The device has been attached and low level communications established.
 */
public class AttachedState extends AbstractManagementState {
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
