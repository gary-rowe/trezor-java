package uk.co.froot.trezorjava.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.UsbDeviceDescriptor;

public class DefaultTrezorEventHandler implements TrezorEventHandler {

  private static final Logger log = LoggerFactory.getLogger(DefaultTrezorEventHandler.class);

  @Override
  public void onDeviceAttached(TrezorType trezorType, UsbDeviceDescriptor descriptor) {

  }

  @Override
  public void onDeviceDetached(TrezorType trezorType, UsbDeviceDescriptor descriptor) {

  }

  @Override
  public void onDeviceFailed(TrezorType trezorType, UsbDeviceDescriptor descriptor) {
  }

  @Override
  public void showDeviceReady() {

  }

  @Override
  public void showDeviceDetached() {

  }

  @Override
  public void showDeviceStopped() {

  }

  @Override
  public void showButtonPress() {

  }

  @Override
  public void showWordEntry() {

  }

  @Override
  public void showPinEntry() {

  }

  @Override
  public void showPassphraseEntry() {

  }

  @Override
  public void showOperationSucceeded() {

  }

  @Override
  public void showOperationFailed() {

  }

  @Override
  public void provideEntropy() {

  }

  @Override
  public void onAddressGenerated() {

  }

  @Override
  public void onPublicKeyGenerated() {

  }

  @Override
  public void onPublicKeyForIdentity() {

  }

  @Override
  public void onMessageSignature() {

  }

  @Override
  public void onDeterministicHierarchy() {

  }

  @Override
  public void onSignedIdentity() {

  }
}

