package uk.co.froot.trezorjava.core;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import com.satoshilabs.trezor.lib.protobuf.TrezorType;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

import javax.usb.UsbException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Provides a collection of simple examples to demonstrate low level communication with the Trezor device.
 */
public class Main {

  public static void main(String[] args) throws InvalidProtocolBufferException, UsbException {

    TrezorManager trezorManager = new TrezorManager();

    // Read commands and execute them
    System.out.println("\n"+ConsoleUtils.ANSI_BLUE + "Welcome to the Trezor CLI." + ConsoleUtils.ANSI_RESET + "\nPlease use the menu below to explore.\n");
    printHelp();

    boolean exit = false;
    while (!exit) {
      Message response;
      System.out.print("Command: ");
      char key = readKey();
      switch (key) {
        case '0':
          response = sendInitialize(trezorManager);
          break;
        case '1':
          response = sendPing(trezorManager);
          break;
        case 'h':
          printHelp();
        case 'q':
          exit = true;
          continue;
        default:
          continue;
      }

      if (response instanceof TrezorMessage.Success) {
        System.out.println(ConsoleUtils.ANSI_GREEN + "SUCCESS" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((TrezorMessage.Success) response).getMessage());
      }
      if (response instanceof TrezorMessage.Failure) {
        System.out.println(ConsoleUtils.ANSI_RED + "FAILURE" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((TrezorMessage.Failure) response).getMessage());
      }
      if (response instanceof TrezorMessage.Features) {
        TrezorMessage.Features features = (TrezorMessage.Features) response;
        System.out.println(ConsoleUtils.ANSI_GREEN + "FEATURES" + ConsoleUtils.ANSI_RESET);
        System.out.println("Device Id: " + features.getDeviceId());
        System.out.println("Label: " + features.getLabel());
        System.out.println("Model: " + features.getModel());
        System.out.println("Initialized: " + features.getInitialized());
        System.out.println("Coin types:");
        for (TrezorType.CoinType coinType : features.getCoinsList()) {
          System.out.println(coinType.getCoinName());
        }
      }

    }

    // Clean up resources
    trezorManager.close();

  }

  private static void printHelp() {
    System.out.println("Menu");
    System.out.println("0 - Send Initialize");
    System.out.println("1 - Send Ping");
    System.out.println("1 - Send Wipe");
    System.out.println("h - Help (this menu)");
    System.out.println("q - Quit");
  }

  /**
   * Read a key from stdin and returns it.
   *
   * @return The read key.
   */
  private static char readKey() {
    try {
      String line =
        new BufferedReader(new InputStreamReader(System.in)).readLine();
      if (line.length() > 0) return line.charAt(0);
      return 0;
    } catch (IOException e) {
      throw new RuntimeException("Unable to read key", e);
    }
  }

  private static Message sendInitialize(TrezorManager trezorManager) throws InvalidProtocolBufferException {
    TrezorMessage.Initialize message = TrezorMessage.Initialize.newBuilder().build();
    return trezorManager.sendMessage(message);
  }

  private static Message sendPing(TrezorManager trezorManager) throws InvalidProtocolBufferException {
    TrezorMessage.Ping message = TrezorMessage.Ping.newBuilder().setMessage("Pong!").build();
    return trezorManager.sendMessage(message);
  }

}
