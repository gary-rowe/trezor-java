package uk.co.froot.trezorjava;

import org.usb4java.*;
import uk.co.froot.trezorjava.events.UsbEventHandlerThread;
import uk.co.froot.trezorjava.events.UsbHotplugCallback;

/**
 * Manager class to provide the following:
 *
 * Access to the top level Trezor API.
 *
 */
public class TrezorManager
{

  /**
   * TrezorManager method.
   *
   * @param args
   *            Command-line arguments (Ignored)
   * @throws Exception
   *             When something goes wrong.
   */
  public static void main(String[] args) throws Exception
  {
    initLibUsb();
    int result;


    // Start the event handling thread
    UsbEventHandlerThread thread = new UsbEventHandlerThread();
    thread.start();

    // Register the hotplug callback
    HotplugCallbackHandle callbackHandle = new HotplugCallbackHandle();
    result = LibUsb.hotplugRegisterCallback(null,
      LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED
        | LibUsb.HOTPLUG_EVENT_DEVICE_LEFT,
      LibUsb.HOTPLUG_ENUMERATE,
      LibUsb.HOTPLUG_MATCH_ANY,
      LibUsb.HOTPLUG_MATCH_ANY,
      LibUsb.HOTPLUG_MATCH_ANY,
      new UsbHotplugCallback(),
      null,
      callbackHandle
    );
    if (result != LibUsb.SUCCESS)
    {
      throw new LibUsbException("Unable to register hotplug callback",
        result);
    }

    // Our faked application. Hit enter key to exit the application.
    System.out.println("Hit enter to exit the demo");
    System.in.read();

    // Unregister the hotplug callback and stop the event handling thread
    thread.abort();
    LibUsb.hotplugDeregisterCallback(null, callbackHandle);
    thread.join();

    // Deinitialize the libusb context
    LibUsb.exit(null);
  }

  /**
   * Initialise libusb context.
   */
  private static void initLibUsb() {

    int result = LibUsb.init(null);
    if (result != LibUsb.SUCCESS)
    {
      throw new LibUsbException("Unable to initialize libusb.", result);
    }

    // Require hotplug capability
    if (!LibUsb.hasCapability(LibUsb.CAP_HAS_HOTPLUG))
    {
      throw new LibUsbException("Unable to register hotplug callback (not supported).", LibUsb.ERROR_NOT_SUPPORTED);
    }
  }
}
