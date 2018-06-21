package uk.co.froot.trezorjava.core;

/**
 * Downstream API consumers should implement this to listen for events coming from the device.
 */
public interface TrezorEventListener {

  /**
   * Handles the receipt of a Trezor event.
   *
   * @param event The Trezor event containing metadata.
   */
  void onTrezorEvent(TrezorEvent event);

}
