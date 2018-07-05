package uk.co.froot.trezorjava.core.exceptions;

/**
 * Exception to indicate an unknown Trezor device has been connected.
 */
public class UnknownDeviceTrezorException extends TrezorException {
  public UnknownDeviceTrezorException(String message, Throwable t) {
    super(message, t);
  }
  public UnknownDeviceTrezorException(String message) {
    super(message);
  }
}
