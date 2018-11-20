package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * The device has been attached and low level communications established.
 */
public class AttachedState extends AbstractManagementState {
  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {

    // Do nothing - the FSM will trigger a transition

  }

  @Override
  public void doExit(TrezorDeviceManager deviceManager) {

  }

}
