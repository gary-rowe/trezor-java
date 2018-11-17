package uk.co.froot.trezorjava.examples.v2.service;

import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

/**
 * <p>Connect to a Trezor using the service API and send a ping. Await response.</p>
 *
 * @since 0.0.1
 *  
 */
public class PingExample extends AbstractServiceExample {

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required.
   *
   */
  public static void main(String[] args) {

    // Create a service and register this as the event listener
    PingExample exampleListener = new PingExample();
    TrezorService service = TrezorServices.awaitDevice(exampleListener);

    // Example code starts here

    // Examples maintain a reference to the service in addition to being listeners
    exampleListener.setService(service);

    // Start the ping
    service.ping();

  }

  @Override
  void internalOnTrezorEvent(TrezorEvent event) {

    log.debug(event.toString());

  }
}
