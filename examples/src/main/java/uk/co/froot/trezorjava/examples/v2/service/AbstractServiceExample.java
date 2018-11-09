package uk.co.froot.trezorjava.examples.v2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.service.TrezorService;

public abstract class AbstractServiceExample  implements TrezorEventListener {

  static final Logger log = LoggerFactory.getLogger(FeaturesExample.class);
  private TrezorService service;

  @Override
  public void onTrezorEvent(TrezorEvent event) {

    log.debug("Received message event: '{}'", event.getMessage());

    // Provide default handle of common events.
    // Hand over to implementation specific handling.
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
        log.info("Device has attached. Expecting a connection attempt.");
        break;
      case DEVICE_CONNECTED:
        log.info("Device has connected.");
        internalOnTrezorEvent(event);
        break;
      default:
        log.info("Unexpected message from device. Exiting.");
        System.exit(0);
    }

  }

  /**
   * Example specific handling of events
   * @param event The original event from the device.
   */
  abstract void internalOnTrezorEvent(TrezorEvent event);

  void setService(TrezorService service) {
    this.service = service;
  }

  public TrezorService getService() {
    return service;
  }

}
