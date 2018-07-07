package uk.co.froot.trezorjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;
import uk.co.froot.trezorjava.core.exceptions.TrezorException;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;

public class TrezorServices {

  private static final Logger log = LoggerFactory.getLogger(TrezorServices.class);

  /**
   * <p>Await the attachment of a Trezor device and provide a service for high level API access.</p>
   *
   * @param trezorEventListener The Trezor event listener for this device.
   *
   * @return An instance of a suitable service to manage the device.
   */
  public static TrezorService awaitDevice(TrezorEventListener trezorEventListener) throws TrezorException {

    TrezorDeviceManager trezorDeviceManager = new TrezorDeviceManager();

    // Blocks until a device is attached
    trezorDeviceManager.awaitDevice();

    // Determine the appropriate service based on the device type
    switch (trezorDeviceManager.context().trezorType()) {
      // TODO Create support for V1 Trezor device via libusb
//      case V1:
//        return new V1TrezorService(trezorDeviceManager, trezorEventListener);
      case V2_FACTORY:
        return new V2TrezorService(trezorDeviceManager, trezorEventListener);
      case V2:
        return new V2TrezorService(trezorDeviceManager, trezorEventListener);
      default:
        throw new TrezorException("Unknown Trezor device attached.");
    }

  }

}
