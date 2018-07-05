package uk.co.froot.trezorjava.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.froot.trezorjava.core.events.TrezorEvent;
import uk.co.froot.trezorjava.core.events.TrezorEventListener;

/**
 * Default implementation of a Trezor event handler suitable for use as a base class
 * by API consumers.
 */
public class DefaultTrezorEventListener implements TrezorEventListener {

  private static final Logger log = LoggerFactory.getLogger(DefaultTrezorEventListener.class);

  @Override
  public void onTrezorEvent(TrezorEvent event) {

  }
}

