package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * The device has been detached and low level communication has ceased.
 */
public class DetachedState extends AbstractManagementState {

  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {

  }

  @Override
  public void doExit(TrezorDeviceManager deviceManager) {

  }
}
