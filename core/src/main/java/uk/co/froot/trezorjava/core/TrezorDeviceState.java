package uk.co.froot.trezorjava.core;

/**
 * <p>The Trezor device State describes the current situation with the connectivity of the device.</p>
 *
 * @since 0.0.1
 *  
 */
public enum TrezorDeviceState {

  /**
   * A device encountered an error in the environment (e.g. timeout or native library load failure).
   */
  DEVICE_FAILED,
  /**
   * A device is attached (device present but no communications yet attempted at the wire level).
   */
  DEVICE_ATTACHED,
  /**
   * A device is detached (no device present).
   */
  DEVICE_DETACHED,
  /**
   * A device is connected (device present and communications established at the wire level).
   */
  DEVICE_CONNECTED,

  // End of enum
  ;

}
