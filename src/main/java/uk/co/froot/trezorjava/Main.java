package uk.co.froot.trezorjava;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import org.usb4java.LibUsb;
import uk.co.froot.trezorjava.utils.ConsoleUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {

    TrezorManager trezorManager = new TrezorManager();
    trezorManager.initialise();

    // Read commands and execute them
    System.out.println(ConsoleUtils.ANSI_BLUE + "Welcome to the Trezor CLI." + ConsoleUtils.ANSI_RESET + "\nPlease use the menu below to explore.");
    System.out.println("0 - Send Initialize");
    System.out.println("1 - Send Ping");
    System.out.println("q - Quit");

    // TODO Re-instate the menu
//    boolean exit = false;
//    while (!exit) {
//      System.out.print(": ");
//      char key = readKey();
//      switch (key) {
//        case '1':
//          trezorManager.sendPing();
//          break;
//
//        case 'q':
//          exit = true;
//          break;
//
//        default:
//      }
//    }

    try {
      Message response = trezorManager.sendPing();
      if (response instanceof TrezorMessage.Success) {
        System.out.println(ConsoleUtils.ANSI_GREEN + "SUCCESS" + ConsoleUtils.ANSI_RESET + " Message:" + ((TrezorMessage.Success) response).getMessage());
      }
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

    // Clean up libusb resources
    LibUsb.exit(null);

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


}
