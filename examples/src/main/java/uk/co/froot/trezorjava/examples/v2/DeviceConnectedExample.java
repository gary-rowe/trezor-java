package uk.co.froot.trezorjava.examples.v2;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;
import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

/**
 * <p>Connect to a Trezor</p>
 *
 * @since 0.0.1
 *  
 */
public class DeviceConnectedExample implements TrezorEventListener {

  private static final Logger log = LoggerFactory.getLogger(DeviceConnectedExample.class);

  private TrezorService trezorService;

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   * @throws Exception If something goes wrong
   */
  public static void main(String[] args) throws Exception {

    // Create a service and register this as the event listener
    // running on a separate executor for convenience
    TrezorService trezorService = TrezorServices.awaitDevice(new DeviceConnectedExample());

    trezorService.initialise();

  }

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
        TrezorMessage.Features features = trezorService.getFeatures();
        log.info("Features: {}", features);

      default:
        // Ignore
    }

  }
}
