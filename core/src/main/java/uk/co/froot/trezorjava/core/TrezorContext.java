package uk.co.froot.trezorjava.core;

import com.satoshilabs.trezor.lib.protobuf.TrezorMessageManagement;
import uk.co.froot.trezorjava.core.specifications.WalletSpecification;

import static uk.co.froot.trezorjava.core.TrezorDeviceState.DEVICE_ATTACHED;
import static uk.co.froot.trezorjava.core.TrezorDeviceState.DEVICE_DETACHED;

/**
 * <p>Provides metadata about the current Trezor device (connection status, device type etc).</p>
 */
public class TrezorContext {

  // These must be volatile due to the nature of the polling mechanism
  private volatile TrezorType trezorType = TrezorType.UNKNOWN;
  private volatile TrezorMessageManagement.Features features = null;
  private volatile TrezorDeviceState deviceState = DEVICE_DETACHED;
  private volatile TrezorUIState uiState = TrezorUIState.SHOW_DEVICE_DETACHED;
  private WalletSpecification walletSpecification;

  /**
   * Clear all fields back to a detached state.
   */
  public void reset() {
    trezorType = TrezorType.UNKNOWN;
    features = null;
    deviceState = DEVICE_DETACHED;
    uiState = TrezorUIState.SHOW_DEVICE_DETACHED;
  }

  /**
   * @return The device state indicating connectivity at the hardware level.
   */
  public TrezorDeviceState getDeviceState() {
    return deviceState;
  }

  /**
   * @param deviceState The new device state.
   */
  public void setDeviceState(TrezorDeviceState deviceState) {
    this.deviceState = deviceState;
  }

  /**
   * @return The UI state describing what screen the user should be interacting with.
   */
  public TrezorUIState getUiState() {
    return uiState;
  }

  /**
   * @param uiState The new UI state.
   */
  public void setUiState(TrezorUIState uiState) {
    this.uiState = uiState;
  }

  /**
   * @return The Trezor type.
   */
  public TrezorType getTrezorType() {
    return trezorType;
  }

  /**
   * @param trezorType The Trezor type (e.g. "V2" for a "Trezor Model T").
   */
  public void setTrezorType(TrezorType trezorType) {
    this.trezorType = trezorType;
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
    return deviceState == DEVICE_ATTACHED && features.isInitialized();
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

  /**
   *
   * @param walletSpecification The wallet specification to apply.
   */
  public void setWalletSpecification(WalletSpecification walletSpecification) {
    this.walletSpecification = walletSpecification;
  }

  /**
   * @return The current wallet specification
   */
  public WalletSpecification getWalletSpecification() {
    return walletSpecification;
  }
}
