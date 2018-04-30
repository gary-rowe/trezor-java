package uk.co.froot.trezorjava;

import org.junit.Test;
import uk.co.froot.trezorjava.core.utils.ConsoleUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConsoleUtilsTest {

  /**
   * Verifies that a short page is not padded.
   */
  @Test
  public void testShortPage() {

    byte[] bytes = new byte[10];
    for (int i=0; i < bytes.length; i++) {
      bytes[i] = (byte) i;
    }

    String result = ConsoleUtils.formatBytesAsHex(bytes);

    assertThat(result, is("\n\u001B[32m" +
      " 0  1  2  3  4  5  6  7  8  9 " +
      "10 11 12 13 14 15 16 17 18 19 " +
      "20 21 22 23 24 25 26 27 28 29 " +
      "30 31 32 33 34 35 36 37 38 39 " +
      "40 41 42 43 44 45 46 47 48 49 " +
      "50 51 52 53 54 55 56 57 58 59 " +
      "60 61 62 63\n\u001B[0m" +
      "00 01 02 03 04 05 06 07 08 09 ")
    );

  }

  /**
   * Verifies the wrapping at 64 byte page length.
   */
  @Test
  public void testLongPage() {

    byte[] bytes = new byte[74];
    for (int i=0; i < bytes.length; i++) {
      bytes[i] = (byte) i;
    }

    String result = ConsoleUtils.formatBytesAsHex(bytes);

    assertThat(result, is("\n\u001B[32m" +
        " 0  1  2  3  4  5  6  7  8  9 " +
        "10 11 12 13 14 15 16 17 18 19 " +
        "20 21 22 23 24 25 26 27 28 29 " +
        "30 31 32 33 34 35 36 37 38 39 " +
        "40 41 42 43 44 45 46 47 48 49 " +
        "50 51 52 53 54 55 56 57 58 59 " +
        "60 61 62 63\n\u001B[0m" +
        "00 01 02 03 04 05 06 07 08 09 " +
        "0A 0B 0C 0D 0E 0F 10 11 12 13 " +
        "14 15 16 17 18 19 1A 1B 1C 1D " +
        "1E 1F 20 21 22 23 24 25 26 27 " +
        "28 29 2A 2B 2C 2D 2E 2F 30 31 " +
        "32 33 34 35 36 37 38 39 3A 3B " +
        "3C 3D 3E 3F\n" +
        "40 41 42 43 44 45 46 47 48 49 ")
    );

  }

}
