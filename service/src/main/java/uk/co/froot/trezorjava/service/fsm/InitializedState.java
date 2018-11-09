package uk.co.froot.trezorjava.service.fsm;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * The device
 */
public class InitializedState extends AbstractManagementState {

  public void enter(TrezorDeviceManager deviceManager) {

    TrezorMessageManagement.Initialize message = TrezorMessageManagement.Initialize.newBuilder().build();
    deviceManager.sendMessage(message);


  }
}
