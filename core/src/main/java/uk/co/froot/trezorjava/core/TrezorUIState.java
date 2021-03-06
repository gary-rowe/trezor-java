package uk.co.froot.trezorjava.core;

/**
 * <p>The Trezor UI State provides a quick reference to downstream consumers so that
 * the user can be presented with a suitable interface to handle particular use cases.</p>
 *
 * <p>The UI state messages are insufficient on their own and should be used in conjunction with
 * the overall device context to determine on-screen messages and so on.</p>
 *
 * @since 0.0.1
 *  
 */
public enum TrezorUIState {

  // Use cases
  /**
   * Indicates there is a problem with the hardware wallet (incompatible firmware, USB environment etc)
   */
  SHOW_DEVICE_FAILED,

  /**
   * Indicates that a device has been attached.
   * This is prior to interrogation of the device features.
   */
  SHOW_DEVICE_ATTACHED,

  /**
   * Indicates that a device is detached.
   * Further interaction with the device is no longer possible.
   */
  SHOW_DEVICE_DETACHED,

  /**
   * Indicates that a device is ready to be used.
   * The service will provide more details about wallet creation being necessary and so on.
   */
  SHOW_DEVICE_READY,

  /**
   * The UI should show a message informing the user that the device requires a button press.
   */
  SHOW_BUTTON_PRESS,

  /**
   * The UI should show a dialog asking the user to enter a word from their seed phrase.
   */
  SHOW_WORD_ENTRY,

  /**
   * The UI should show a dialog asking the user to enter their PIN (possibly using an obfuscated matrix technique).
   */
  SHOW_PIN_ENTRY,

  /**
   * The UI should show a dialog asking the user to enter their passphrase.
   */
  SHOW_PASSPHRASE_ENTRY,

  /**
   * The UI should show a message telling the user that the operation completed successfully.
   */
  SHOW_OPERATION_SUCCEEDED,

  /**
   * The UI should show a message telling the user that the operation failed to complete (could be their cancellation, unexpected message etc).
   */
  SHOW_OPERATION_FAILED,

  /**
   * An additional source of entropy should be provided that will be combined with that shown on the device to
   * yield a seed phrase that can be proved to contain both values. This reduces the chances of compromised hardware.
   */
  PROVIDE_ENTROPY,

  /**
   * An address has been generated by the device in response to an earlier request
   */
  ADDRESS,

  /**
   * A public key been generated by the device in response to an earlier request
   */
  PUBLIC_KEY,

  /**
   * An identity public key been generated by the device in response to an earlier request
   */
  PUBLIC_KEY_FOR_IDENTITY,

  /**
   * A signed message been generated by the device in response to an earlier request
   */
  MESSAGE_SIGNATURE,

  /**
   * A deterministic hierarchy has been generated by the device in response to an earlier request
   */
  DETERMINISTIC_HIERARCHY,

  /**
   * A signed identity has been generated by the device in response to an earlier request
   */
  SIGNED_IDENTITY,

  // End of enum
  ;

}

