package uk.co.froot.trezorjava.examples.v2.service;

import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

/**
 * <p>Connect to a Trezor using the service API and identify its features.</p>
 *
 * @since 0.0.1
 *  
 */
public class FeaturesExample extends AbstractServiceExample {

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   */
  public static void main(String[] args) {

    // Create a service and register this as the event listener
    FeaturesExample exampleListener = new FeaturesExample();
    TrezorService service = TrezorServices.awaitDevice(exampleListener);
    exampleListener.setService(service);

    service.features();

  }

}
