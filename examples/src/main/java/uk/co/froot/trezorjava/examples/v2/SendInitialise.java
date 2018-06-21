package uk.co.froot.trezorjava.examples.v2;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.TrezorEvent;
import uk.co.froot.trezorjava.core.TrezorEventListener;
import uk.co.froot.trezorjava.core.TrezorEvents;
import uk.co.froot.trezorjava.service.TrezorService;
import uk.co.froot.trezorjava.service.TrezorServices;

import static uk.co.froot.trezorjava.core.TrezorEventType.SHOW_DEVICE_DETACHED;
import static uk.co.froot.trezorjava.core.TrezorEventType.SHOW_DEVICE_FAILED;
import static uk.co.froot.trezorjava.core.TrezorEventType.SHOW_DEVICE_READY;

/**
 * <p>Send Initialise to a V2 Trezor</p>
 *
 * @since 0.0.1
 *  
 */
public class SendInitialise implements TrezorEventListener {

  private static final Logger log = LoggerFactory.getLogger(SendInitialise.class);

  private TrezorService trezorService;

  /**
   * <p>TrezorServices entry point to the example</p>
   *
   * @param args None required
   *
   * @throws Exception If something goes wrong
   */
  public static void main(String[] args) throws Exception {

    // Register this class as a listener for Trezor events
    TrezorEvents.register(new SendInitialise());

    // Await a connection by a Trezor device
    TrezorService trezorService = TrezorServices.awaitDevice();

  }

  @Override
  public void onTrezorEvent(TrezorEvent event) {
    log.debug("Received Trezor event: '{}'", event.getEventType().name());

    switch (event.getEventType()) {
      case SHOW_DEVICE_FAILED:
        // Treat as end of example
        System.exit(0);
        break;
      case SHOW_DEVICE_DETACHED:
        // Can simply wait for another device to be connected again
        break;
      case SHOW_DEVICE_READY:
        // Get some information about the device
        TrezorMessage.Features features = trezorService.getContext().getFeatures().get();
        log.info("Features: {}", features);

      default:
        // Ignore
    }

  }
}
