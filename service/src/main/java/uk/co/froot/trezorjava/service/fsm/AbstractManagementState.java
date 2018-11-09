package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * Abstract base class to support management messages and state progressions
 */
public abstract class AbstractManagementState implements ManagementState {
  @Override
  public void enter(TrezorDeviceManager deviceManager) {

  }
}
