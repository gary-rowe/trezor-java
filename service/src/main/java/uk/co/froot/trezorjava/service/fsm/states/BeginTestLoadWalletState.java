package uk.co.froot.trezorjava.service.fsm.states;

import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import hw.trezor.messages.common.MessagesCommon;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * The device needs to return to the initialized state, cancelling any current operation.
 */
public class BeginTestLoadWalletState extends AbstractManagementState {

  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {

    // Send the Wipe message to the device
    TrezorMessageManagement.WipeDevice message = TrezorMessageManagement.WipeDevice.newBuilder().build();
    deviceManager.sendMessage(message);

  }

  @Override
  public void doExit(TrezorDeviceManager deviceManager) {

  }

  @Override
  public ManagementState lookupStateByEvent(TrezorEvent event) {

    Message message = event.getMessage();

    if (message instanceof MessagesCommon.ButtonRequest) {

      return new ConfirmWipeDeviceState();

    }
    return this;
  }
}
