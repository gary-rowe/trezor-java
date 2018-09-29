package uk.co.froot.trezorjava.examples.v2.service;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

/**
 * <p>Connect to a Trezor using the service API and identify its features.</p>
 *
 * @since 0.0.1
 *  
 */
public class DeviceFeaturesExample implements TrezorEventListener {

  private static final Logger log = LoggerFactory.getLogger(DeviceFeaturesExample.class);
  private TrezorService service;

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   */
  public static void main(String[] args) throws InterruptedException {

    // Create a service and register this as the event listener
    DeviceFeaturesExample exampleListener = new DeviceFeaturesExample();
    TrezorService service = TrezorServices.awaitDevice(exampleListener);
    exampleListener.setService(service);

    service.initialize();

    Thread.sleep(2000);
  }

  // TODO Hook up the TrezorEvents to receive Features message
  @Override
  public void onTrezorEvent(TrezorEvent event) {
    log.debug("Received Trezor event: '{}'", event.getUIState().name());

    switch (event.getDeviceState()) {
      case DEVICE_FAILED:
        // Treat as end of example
        log.info("Device has failed. Exiting.");
        System.exit(0);
        break;
      case DEVICE_DETACHED:
        // Can simply wait for another device to be connected again
        log.info("Device has detached. Waiting.");
        break;
      case DEVICE_ATTACHED:
        // Low level handler will automatically transition to Connected if possible
        log.info("Device has attached. Waiting.");
        break;
      case DEVICE_CONNECTED:
        log.info("Device has connected. Reading features.");
        // Get some information about the device
        TrezorMessageManagement.Features features = service.getFeatures();
        log.info("Features: {}", features.getLabel());

      default:
        // Ignore
    }

  }

  private void setService(TrezorService service) {
    this.service = service;
  }

  public TrezorService getService() {
    return service;
  }
}
