package uk.co.froot.trezorjava.service.fsm;

import hw.trezor.messages.common.MessagesCommon;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.events.TrezorEvent;

/**
 * Wipe device use case.
 *
 * The wipe has been requested and a button press requested.
 */
public class ConfirmWipeDeviceState extends AbstractManagementState {
  @Override
  public void doEnter(TrezorDeviceManager deviceManager) {

    // Send the Initialize message to the device
    MessagesCommon.ButtonAck message = MessagesCommon.ButtonAck.newBuilder().build();
    deviceManager.sendMessage(message);

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
