package uk.co.froot.trezorjava.core.specifications;

/**
 * Defines the parameters to be used when creating a new wallet.
 *
 * NOTE: The secure constructor does not contain any useful information for an attacker, but the insecure constructor provides everything.
 *
 */
public class WalletSpecification {

  private String language = "english";
  private String label = "Abandon";
  private boolean displayRandom = false;
  private boolean pinProtection = true;
  private int strength = 256;

  // Default seed phrase for testing
  private String seedPhrase = "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about";
  // Default PIN for testing
  private String pin = "1";

  /**
   * Secure constructor for use in production.
   *
   * @param language      The language to use (e.g. "english")
   * @param label         The label to display below the logo (e.g "Fred")
   * @param displayRandom True if the device should display the entropy generated by the device before asking for additional entropy
   * @param pinProtection True if the device should use PIN protection
   * @param strength      The bits of entropy in the seed phrase (128, 192, 256) corresponding to 12, 18, 24 words
   */
  public WalletSpecification(
    String language,
    String label,
    boolean displayRandom,
    boolean pinProtection,
    int strength
  ) {
    this.language = language;
    this.label = label;
    this.displayRandom = displayRandom;
    this.pinProtection = pinProtection;
    this.strength = strength;
  }

  /**
   * Insecure constructor for use in TESTING only.
   *
   * A seed phrase should always be generated by the device.
   *
   * @param language   The language (e.g. "english")
   * @param seedPhrase The seed phrase provided by the user in the clear
   * @param pin        The personal identification number (PIN) in the clear
   */
  public WalletSpecification(String language, String seedPhrase, String pin) {
    this.language = language;
    this.seedPhrase = seedPhrase;
    this.pin = pin;
  }

  /**
   * @return The language to use (e.g. "english").
   */
  public String getLanguage() {
    return language;
  }

  /**
   * @return The label to display below the logo (e.g "Fred").
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return True if the device should display the entropy generated by the device before asking for additional entropy.
   */
  public boolean isDisplayRandom() {
    return displayRandom;
  }

  /**
   * @return True if the device should use PIN protection.
   */
  public boolean isPinProtection() {
    return pinProtection;
  }

  /**
   * @return The bits of entropy in the seed phrase.
   */
  public int getStrength() {
    return strength;
  }

  /**
   * @return The seed phrase in the clear.
   */
  public String getSeedPhrase() {
    return seedPhrase;
  }

  /**
   * @return The personal identification number (PIN) in the clear.
   */
  public String getPin() {
    return pin;
  }


}
