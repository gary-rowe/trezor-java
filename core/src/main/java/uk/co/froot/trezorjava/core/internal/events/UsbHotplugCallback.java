package uk.co.froot.trezorjava.core.internal.events;

import org.usb4java.*;

/**
 * Callback for USB hot-plugging events.
 */
public class UsbHotplugCallback implements HotplugCallback {
  @Override
  public int processEvent(Context context, Device device, int event,
                          Object userData) {

    // Determine what connected
    DeviceDescriptor descriptor = new DeviceDescriptor();
    int result = LibUsb.getDeviceDescriptor(device, descriptor);

    // Fail fast
    if (result != LibUsb.SUCCESS)
      throw new LibUsbException("Unable to read device descriptor", result);

    if (descriptor.idVendor() == 0x1209 && descriptor.idProduct() == 0x53c0) {
      System.out.format("%s: Trezor Model T%n",
        event == LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED ? "Connected" : "Disconnected"
      );
    } else {
      System.out.format("%s: %04x:%04x%n",
        event == LibUsb.HOTPLUG_EVENT_DEVICE_ARRIVED ? "Connected" : "Disconnected",
        descriptor.idVendor(),
        descriptor.idProduct()
      );
    }

    return 0;
  }
}
