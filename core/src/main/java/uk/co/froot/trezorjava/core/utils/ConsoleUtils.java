package uk.co.froot.trezorjava.core.utils;

public class ConsoleUtils {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
  private final static String BYTE_HEADER_64 = "\n"
    + ANSI_GREEN +
    " 0  1  2  3  4  5  6  7  8  9 " +
    "10 11 12 13 14 15 16 17 18 19 " +
    "20 21 22 23 24 25 26 27 28 29 " +
    "30 31 32 33 34 35 36 37 38 39 " +
    "40 41 42 43 44 45 46 47 48 49 " +
    "50 51 52 53 54 55 56 57 58 59 " +
    "60 61 62 63"
    + ANSI_RESET;

  /**
   * Formats a continuous byte array into a collection of 64 byte rows with a header
   * to assist USB packet debugging.
   *
   * @param bytes      The byte array.
   * @param showHeader True if a position header is required.
   *
   * @return A string containing the data suitable for console display.
   */
  public static String formatBytesAsHex(byte[] bytes, boolean showHeader) {
    char[] hexChars = new char[bytes.length * 3];

    // Build char array (fast)
    for (int i = 0; i < bytes.length; i++) {
      // Get the value
      int value = bytes[i] & 0xFF;
      if (i < 64) {
        // Convert to hex with trailing space for first row
        hexChars[i * 3] = hexArray[value >>> 4];
        hexChars[i * 3 + 1] = hexArray[value & 0x0F];
        hexChars[i * 3 + 2] = ' ';
      }
      if (i % 64 == 0) {
        hexChars[i * 3] = '\n';
        hexChars[i * 3 + 1] = hexArray[value >>> 4];
        hexChars[i * 3 + 2] = hexArray[value & 0x0F];
      } else {
        hexChars[i * 3] = ' ';
        hexChars[i * 3 + 1] = hexArray[value >>> 4];
        hexChars[i * 3 + 2] = hexArray[value & 0x0F];
      }
    }

    // Combine as string
    return showHeader ? BYTE_HEADER_64 + new String(hexChars) : new String(hexChars);
  }
}
