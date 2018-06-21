package uk.co.froot.trezorjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorManager;

import javax.usb.UsbException;

public class TrezorServices {

  private static final Logger log = LoggerFactory.getLogger(TrezorServices.class);

  /**
   * Await the connection the connection of a Trezor device.
   * @return An instance of a suitable service to manage the device.
   */
  public static TrezorService awaitDevice() {

    try {
      TrezorManager trezorManager = new TrezorManager();
    } catch (UsbException e) {
      e.printStackTrace();
    }

    return null;
  }

}
