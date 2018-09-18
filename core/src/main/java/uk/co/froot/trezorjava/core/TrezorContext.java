package uk.co.froot.trezorjava.core;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;

/**
 * <p>Provides metadata about the current Trezor device (connection status, device type etc).</p>
 */
public class TrezorContext {

  // These must be volatile due to the nature of the polling mechanism
  private volatile boolean deviceAttached = false;
  private volatile TrezorType trezorType = TrezorType.UNKNOWN;
  private volatile TrezorMessageManagement.Features features = null;

  /**
   * Clear all fields back to a detached state.
   */
  public void reset() {
    deviceAttached = false;
    trezorType = TrezorType.UNKNOWN;
    features = null;
  }

  /**
   * @param deviceAttached True if a Trezor device is attached and opened for USB communication.
   */
  public void setDeviceAttached(boolean deviceAttached) {
    this.deviceAttached = deviceAttached;
  }

  /**
   * @return True if a Trezor device is attached and opened for USB communication.
   */
  public boolean isDeviceAttached() {
    return deviceAttached;
  }

  /**
   * @param trezorType The Trezor type (e.g. "V2" for a "Trezor Model T").
   */
  public void setTrezorType(TrezorType trezorType) {
    this.trezorType = trezorType;
  }

  /**
   * @return The Trezor type.
   */
  public TrezorType trezorType() {
    return trezorType;
  }

  /**
   * @return True if device is attached, opened for USB communication and has a wallet in place.
   */
  public boolean isDeviceInitialised() {
    // Fail fast
    if (features == null) {
      return false;
    }
    // A device is initialised if it is both attached and Features indicate the case
    return isDeviceAttached() && features.isInitialized();
  }

  /**
   * @param features The Trezor device Features message.
   */
  public void setFeatures(TrezorMessageManagement.Features features) {
    this.features = features;
  }

  /**
   * @return The Trezor device Features message.
   */
  public TrezorMessageManagement.Features getFeatures() {
    return features;
  }
}
