package uk.co.froot.trezorjava.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorManager;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    TrezorManager trezorManager = new TrezorManager();
    trezorManager.initialise();

    try {
      Message response = trezorManager.sendPing();
      if (response instanceof TrezorMessage.Success) {
        System.out.println(ConsoleUtils.ANSI_GREEN + "SUCCESS" + ConsoleUtils.ANSI_RESET + " Message:" + ((TrezorMessage.Success) response).getMessage());
      }
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

  }

}
