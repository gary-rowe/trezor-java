package uk.co.froot.trezorjava.service.fsm.states;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * The device needs to return to the initialized state, cancelling any current operation.
 */
public class InitializedState extends AbstractManagementState {

  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {

    // Send the Initialize message to the device
    TrezorMessageManagement.Initialize message = TrezorMessageManagement.Initialize.newBuilder().build();
    deviceManager.sendMessage(message);

  }

  @Override
  public void doExit(TrezorDeviceManager deviceManager) {

  }

  @Override
  public ManagementState lookupStateByEvent(TrezorEvent event) {

    // TODO Determine appropriate events
    return this;
  }
}
