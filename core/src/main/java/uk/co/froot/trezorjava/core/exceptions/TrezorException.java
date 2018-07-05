package uk.co.froot.trezorjava.core.exceptions;

/**
 * Exception to indicate runtime failures with Trezor devices.
 */
public class TrezorException extends RuntimeException {
  public TrezorException(String message, Throwable t) {
    super(message, t);
  }
  public TrezorException(String message) {
    super(message);
  }
}
