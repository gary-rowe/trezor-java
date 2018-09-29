package uk.co.froot.trezorjava.examples.v2.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorDeviceManager;

/**
 * <p>Connect to a Trezor using the core API and identify its type.</p>
 *
 * @since 0.0.1
 *  
 */
public class DeviceConnectedExample {

  private static final Logger log = LoggerFactory.getLogger(DeviceConnectedExample.class);

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   */
  public static void main(String[] args) {

    // Creating the device manager initiates the USB library
    TrezorDeviceManager trezorDeviceManager = new TrezorDeviceManager();

    // Blocks until a device is attached
    trezorDeviceManager.awaitDevice();

    log.info("Device attached is {}", trezorDeviceManager.context().trezorType());

  }

}
