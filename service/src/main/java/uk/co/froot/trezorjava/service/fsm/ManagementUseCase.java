package uk.co.froot.trezorjava.service.fsm;

public enum ManagementUseCase {

  /**
   * Await device attachment.
   */
  ATTACH_DEVICE_BEGIN,
  /**
   * Get device features.
   */
  ATTACH_DEVICE_FEATURES,
  /**
   * Begin device wipe.
   */
  WIPE_DEVICE_BEGIN,
  /**
   * Await wipe device confirmation.
   */
  WIPE_DEVICE_CONFIRM

  // End of enum
  ;

}
