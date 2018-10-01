package uk.co.froot.trezorjava.examples.v2.service;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.service.TrezorService;

public class AbstractServiceExample  implements TrezorEventListener {

  protected static final Logger log = LoggerFactory.getLogger(FeaturesExample.class);
  private TrezorService service;

  // TODO Hook up the TrezorEvents to receive Features message
  @Override
  public void onTrezorEvent(TrezorEvent event) {
    log.debug("Received message event: '{}'", event.getMessage());

    switch (event.getDeviceManager().context().getDeviceState()) {
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

  void setService(TrezorService service) {
    this.service = service;
  }

  public TrezorService getService() {
    return service;
  }

}
