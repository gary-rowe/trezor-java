package uk.co.froot.trezorjava.examples.v2.service;

import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

/**
 * <p>Connect to a Trezor using the service API and initiate a reset back to factory settings (wipe).</p>
 *
 * @since 0.0.1
 *  
 */
public class WipeDeviceExample extends AbstractServiceExample {

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   */
  public static void main(String[] args) {

    // Create a service and register this as the event listener.
    WipeDeviceExample exampleListener = new WipeDeviceExample();
    TrezorService service = TrezorServices.awaitDevice(exampleListener);
    exampleListener.setService(service);

    // Request device wipe.
    service.wipeDevice();

  }

  @Override
  void internalOnTrezorEvent(TrezorEvent event) {

    log.debug(event.toString());

  }

}