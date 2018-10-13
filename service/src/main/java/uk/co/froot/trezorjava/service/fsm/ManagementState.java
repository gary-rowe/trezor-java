package uk.co.froot.trezorjava.service.fsm;

import uk.co.froot.trezorjava.core.TrezorDeviceManager;

public interface ManagementState {

  void enter(TrezorDeviceManager deviceManager);
}
