package uk.co.froot.trezorjava.core;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import hw.trezor.messages.common.MessagesCommon;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.core.events.TrezorEvents;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Provides a collection of simple examples to demonstrate low level communication with the Trezor device.
 */
public class LowLevelApiExample implements TrezorEventListener {

  public static void main(String[] args) throws InvalidProtocolBufferException {

    LowLevelApiExample example = new LowLevelApiExample();

    // Register for Trezor events
    TrezorEvents.register(example);

    // Start the CLI
    example.run();

  }

  public void run() throws InvalidProtocolBufferException {

    // Trezor device manager runs on main thread
    TrezorDeviceManager trezorDeviceManager = new TrezorDeviceManager();

    System.out.println("\n"+ConsoleUtils.ANSI_BLUE + "Welcome to the Trezor CLI." + ConsoleUtils.ANSI_RESET + "\n");

    // Blocking method until a device is attached
    trezorDeviceManager.awaitDevice();

    System.out.println("\n"+ConsoleUtils.ANSI_GREEN + "Trezor attached: " + ConsoleUtils.ANSI_RESET + "\n" + trezorDeviceManager.context().getTrezorType().name());

    // Read commands and execute them
    System.out.println("\nPlease use the menu below to explore.\n");
    printHelp();

    boolean exit = false;
    while (!exit) {
      System.out.print("Command: ");
      char key = readKey();
      switch (key) {
        case '0':
          sendInitialize(trezorDeviceManager);
          break;
        case '1':
          sendPing(trezorDeviceManager);
          break;
        case 'h':
          printHelp();
        case 'q':
          exit = true;
      }

    } // End of CLI loop

    // Exiting so clean up resources
    trezorDeviceManager.close();

  }

  private void printHelp() {
    System.out.println("Menu");
    System.out.println("0 - Send Initialize");
    System.out.println("1 - Send Ping");
    System.out.println("h - Help (this menu)");
    System.out.println("q - Quit");
  }

  /**
   * Read a key from stdin and returns it.
   *
   * @return The read key.
   */
  private char readKey() {
    try {
      String line =
        new BufferedReader(new InputStreamReader(System.in)).readLine();
      if (line.length() > 0) return line.charAt(0);
      return 0;
    } catch (IOException e) {
      throw new RuntimeException("Unable to read key", e);
    }
  }

  private void sendInitialize(TrezorDeviceManager trezorDeviceManager) throws InvalidProtocolBufferException {
    TrezorMessageManagement.Initialize message = TrezorMessageManagement.Initialize.newBuilder().build();
    trezorDeviceManager.sendMessage(message);
  }

  private void sendPing(TrezorDeviceManager trezorDeviceManager) throws InvalidProtocolBufferException {
    TrezorMessageManagement.Ping message = TrezorMessageManagement.Ping.newBuilder().setMessage("Pong!").build();
    trezorDeviceManager.sendMessage(message);
  }

  @Override
  public void onTrezorEvent(TrezorEvent event) {

    Message message = event.getMessage();

    if (message == null) {
      // Do nothing
      return;
    }
    if (message instanceof MessagesCommon.Success) {
      System.out.println(ConsoleUtils.ANSI_GREEN + "SUCCESS" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((MessagesCommon.Success) message).getMessage());
    }
    if (message instanceof MessagesCommon.Failure) {
      System.out.println(ConsoleUtils.ANSI_RED + "FAILURE" + ConsoleUtils.ANSI_RESET + "\nMessage:" + ((MessagesCommon.Failure) message).getMessage());
    }
    if (message instanceof TrezorMessageManagement.Features) {
      TrezorMessageManagement.Features features = (TrezorMessageManagement.Features) message;
      System.out.println(ConsoleUtils.ANSI_GREEN + "FEATURES" + ConsoleUtils.ANSI_RESET);
      System.out.println("Device Id: " + features.getDeviceId());
      System.out.println("Label: " + features.getLabel());
      System.out.println("Model: " + features.getModel());
      System.out.println("Initialized: " + features.getInitialized());
    }

  }
}
