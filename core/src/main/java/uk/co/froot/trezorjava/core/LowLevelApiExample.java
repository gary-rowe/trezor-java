package uk.co.froot.trezorjava.core;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import hw.trezor.messages.common.MessagesCommon;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

import javax.usb.UsbException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides a collection of simple examples to demonstrate low level communication with the Trezor device.
 */
public class LowLevelApiExample {

  private static final ExecutorService trezorManagerService = Executors.newSingleThreadExecutor();

  public static void main(String[] args) throws InvalidProtocolBufferException, UsbException {

    // Trezor device manager runs on main thread
    TrezorDeviceManager trezorDeviceManager = new TrezorDeviceManager();

    System.out.println("\n"+ConsoleUtils.ANSI_BLUE + "Welcome to the Trezor CLI." + ConsoleUtils.ANSI_RESET + "\n");

    // Blocking method until a device is attached
    trezorDeviceManager.awaitDevice();

    System.out.println("\n"+ConsoleUtils.ANSI_GREEN + "Trezor attached: " + ConsoleUtils.ANSI_RESET + "\n" + trezorDeviceManager.context().trezorType().name());

    // Read commands and execute them
    System.out.println("\nPlease use the menu below to explore.\n");
    printHelp();

    boolean exit = false;
    while (!exit) {
      Message response;
      System.out.print("Command: ");
      char key = readKey();
      switch (key) {
        case '0':
          response = sendInitialize(trezorDeviceManager);
          break;
        case '1':
          response = sendPing(trezorDeviceManager);
          break;
        case 'h':
          printHelp();
        case 'q':
          exit = true;
          continue;
        default:
          continue;
      }

      if (response == null) {
        continue;
      }
      if (response instanceof MessagesCommon.Success) {
        System.out.println(ConsoleUtils.ANSI_GREEN + "SUCCESS" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((MessagesCommon.Success) response).getMessage());
      }
      if (response instanceof MessagesCommon.Failure) {
        System.out.println(ConsoleUtils.ANSI_RED + "FAILURE" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((MessagesCommon.Failure) response).getMessage());
      }
      if (response instanceof TrezorMessageManagement.Features) {
        TrezorMessageManagement.Features features = (TrezorMessageManagement.Features) response;
        System.out.println(ConsoleUtils.ANSI_GREEN + "FEATURES" + ConsoleUtils.ANSI_RESET);
        System.out.println("Device Id: " + features.getDeviceId());
        System.out.println("Label: " + features.getLabel());
        System.out.println("Model: " + features.getModel());
        System.out.println("Initialized: " + features.getInitialized());
      }

    } // End of CLI loop

    // Exiting so clean up resources
    trezorDeviceManager.close();

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

  private static Message sendInitialize(TrezorDeviceManager trezorDeviceManager) throws InvalidProtocolBufferException {
    TrezorMessageManagement.Initialize message = TrezorMessageManagement.Initialize.newBuilder().build();
    return trezorDeviceManager.sendMessage(message);
  }

  private static Message sendPing(TrezorDeviceManager trezorDeviceManager) throws InvalidProtocolBufferException {
    TrezorMessageManagement.Ping message = TrezorMessageManagement.Ping.newBuilder().setMessage("Pong!").build();
    return trezorDeviceManager.sendMessage(message);
  }

}
