package uk.co.froot.trezorjava.examples.v2.service;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import uk.co.froot.trezorjava.core.TrezorDeviceState;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
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
  public static void main(String[] args) throws InterruptedException {

    // Create a service and register this as the event listener
    FeaturesExample exampleListener = new FeaturesExample();
    TrezorService service = TrezorServices.awaitDevice(exampleListener);

    // Example code starts here

    // Examples maintain a reference to the service in addition to being listeners
    exampleListener.setService(service);

    service.initialize();
  }

  @Override
  void internalOnTrezorEvent(TrezorEvent event) {

    // Features are only available after an initial handshake has taken place
    // so we filter accordingly
    if (event.getDeviceManager().context().getDeviceState() == TrezorDeviceState.DEVICE_CONNECTED) {
      // Request features
      TrezorMessageManagement.Features features = getService().features();
      log.info("Features: {}", features.getLabel());
    } else {
      log.info("Device event with state: {}", event.getDeviceManager().context().getDeviceState());
    }

  }

}
