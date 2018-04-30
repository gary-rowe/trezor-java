package uk.co.froot.trezorjava.core.internal.events;

import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

/**
 * USB event handling thread for libusb.
 *
 */
public class UsbEventHandlerThread extends Thread
{

  private volatile boolean abort = false;

  /**
   * Aborts the event handling thread.
   */
  public void abort()
  {
    this.abort = true;
  }

  @Override
  public void run()
  {
    // Loop continuously
    while (!this.abort)
    {
      // Block until libusb has completed event processing or the specified
      // number of microseconds has elapsed.
      int result = LibUsb.handleEventsTimeout(null, 1_000_000);
      if (result != LibUsb.SUCCESS)
        throw new LibUsbException("Unable to handle events", result);
    }
  }
}