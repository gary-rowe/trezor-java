package uk.co.froot.trezorjava.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.UsbDeviceDescriptor;

public class DefaultTrezorEventHandler implements TrezorEventHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultTrezorEventHandler.class);

  @Override
  public void handleDeviceAttached(TrezorType trezorType, UsbDeviceDescriptor descriptor) {
    log.info("Device attached: {}", trezorType);
  }

  @Override
  public void handleDeviceDetached(TrezorType trezorType, UsbDeviceDescriptor descriptor) {
    log.info("Device detached: {}", trezorType);
  }
}
