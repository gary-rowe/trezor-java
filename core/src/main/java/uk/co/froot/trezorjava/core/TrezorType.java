package uk.co.froot.trezorjava.core;

/**
 * <p>Describes the various types of Trezor device compatible with this library.</p>
 */
public enum TrezorType {

  /**
   * A Trezor One (first generation) device.
   */
  V1("Trezor ONE"),

  /**
   * A Trezor Model T (second generation) with no firmware loaded.
   */
  V2_FACTORY("Trezor Model T (awaiting firmware)"),

  /**
   * A Trezor Model T (second generation) with firmware loaded.
   */
  V2("Trezor Model T"),

  /**
   * An unknown device.
   */
  UNKNOWN("Unknown device")

  // End of enum
  ;

  private String name;

  TrezorType(String name) {
    this.name = name;
  }

  /**
   * @return The friendly name for the device (not localised).
   */
  public String getName() {
    return name;
  }
}
