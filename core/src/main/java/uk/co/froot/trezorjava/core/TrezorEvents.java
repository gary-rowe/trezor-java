package uk.co.froot.trezorjava.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread safe Trezor event notification.
 */
public class TrezorEvents {

  private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

  private static final Lock readLock = readWriteLock.readLock();
  private static final Lock writeLock = readWriteLock.writeLock();

  private static final List<TrezorEventListener> listeners = new ArrayList<>();

  /**
   * @param listener The Trezor event listener to register.
   */
  public static void register(TrezorEventListener listener) {

    writeLock.lock();
    try {
      listeners.add(listener);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * @param listener The Trezor event listener to unregister.
   */
  public static void unregister(TrezorEventListener listener) {

    writeLock.lock();
    try {
      listeners.remove(listener);
    } finally {
      writeLock.unlock();
    }

  }

  /**
   * @param trezorEvent The Trezor event to broadcast to listeners.
   */
  public static void notify(TrezorEvent trezorEvent) {

    readLock.lock();
    try {
      listeners.forEach(listener -> listener.onTrezorEvent(trezorEvent));
    } finally {
      readLock.unlock();
    }

  }
}