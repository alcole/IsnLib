/**
 * MIT License
 *
 * <p>Copyright (c) 2016 Alexander Cole
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.alcole.bibliotools;

import java.util.regex.Pattern;

/**
 * This class consists of static methods that can be used when working with bibliographic
 * identifiers, that is the International Standard Numbers for Books(ISBN), Serials(ISSN) and
 * written music(ISMN). 'isn' is used throughout to mean any of ISBN, ISSN, ISMN
 *
 * @author Alex Cole
 */
public final class IsnLib {

  private static final int ISSN_LENGTH = 8;
  private static final int ISBN10_LENGTH = 10;
  private static final int ISBN13_LENGTH = 13;

  private static final String ISSN_PATTERN = "\\d{4}[-]?\\d{3}[0-9xX]{1}";
  private static final String ISBN10_PATTERN = "[\\d|\\-]{9,13}[0-9xX]{1}";
  private static final String EAN13_PATTERN = "[9]{1}[7]{1}[7|8|9][\\d|\\-]{9,13}[\\d]{1}";

  private static final String ISSNEAN13_PREFIX = "977";
  private static final String ISMN_PREFIX = "9790";
  private static final String ISBN_PREFIX1 = "978";
  private static final String ISBN_PREFIX2 = "979";

  private IsnLib() {
    // not called
  }

  /**
   * Strips the hyphens.
   *
   * @param isn The ISN to format
   * @return the string with hyphens removed
   */
  public static String canonicalForm(final String isn) {
    return isn.replace("-", "").trim();
  }

  /**
   * pads with leading zeroes to desired length works only with ISSN and ISBN-10.
   *
   * @param isn the ISN to pad
   * @param length the target length
   * @return the isn string padded with zeroes
   * @throws IllegalArgumentException if provided length is not a valid isn length
   */
  public static String pad(final String isn, final int length) throws IllegalArgumentException {
    if (!(length == ISSN_LENGTH || length == ISBN10_LENGTH)) {
      throw new IllegalArgumentException();
    }
    String padded = isn;
    while (padded.length() < length) {
      padded = "0" + padded;
    }
    return padded;
  }

  /**
   * takes a String and checks if the check digit matches calculated check digit.
   *
   * @param isn the ISN to validate
   * @return true for isn strings with supplied with valid check digit, false otherwise
   */
  public static boolean isValidIsn(final String isn) {
    if (!(Pattern.matches(ISSN_PATTERN, isn)
        || Pattern.matches(ISBN10_PATTERN, isn)
        || Pattern.matches(EAN13_PATTERN, isn))) {
      return false;
    }

    String canon = canonicalForm(isn);

    if (canon.length() == ISBN13_LENGTH) {
      return generateCheck(canon) == canon.charAt(12);
    } else if (canon.length() == ISBN10_LENGTH) {
      return validate(canon, ISBN10_LENGTH);
    } else if (canon.length() == ISSN_LENGTH) {
      return validate(canon, ISSN_LENGTH);
    } else {
      return false;
    }
  }

  /**
   * helper method for validateIsn uses fast accumulator algorithm for ISBN-10 and ISSN.
   *
   * @param isn isn to validate
   * @param length length of isn
   * @return true for isns supplied with valid check digit, false otherwise
   */
  private static boolean validate(final String isn, final int length) {
    if (!(length == ISSN_LENGTH || length == ISBN10_LENGTH)) {
      throw new IllegalArgumentException();
    }

    int s = 0, t = 0;
    for (int i = 0; i < length - 1; i++) {
      t += Character.getNumericValue(isn.charAt(i));
      s += t;
    }
    if (isn.charAt(length - 1) == 'X' || isn.charAt(length - 1) == 'x') {
      t += 10;
    } else {
      t += Character.getNumericValue(isn.charAt(length - 1));
    }
    s += t;
    return (s % 11) == 0;
  }

  /**
   * @param isn the isn with or without the supplied check digit
   * @return the char of the check digit 0-9 or 'X'
   * @throws IllegalArgumentException for strings of the wrong length
   */
  public static char generateCheck(final String isn) throws IllegalArgumentException {
    if (!(isn.length() == ISBN13_LENGTH
        || isn.length() == ISBN10_LENGTH
        || isn.length() == ISBN13_LENGTH - 1
        || isn.length() == ISBN10_LENGTH - 1
        || isn.length() == ISSN_LENGTH
        || isn.length() == ISSN_LENGTH - 1)) throw new IllegalArgumentException();

    int checkSum = 0;
    if (isn.length() == ISBN13_LENGTH || isn.length() == ISBN13_LENGTH - 1) {
      for (int i = 1; i < 12; i = i + 2) {
        checkSum += Character.getNumericValue(isn.charAt(i));
      }
      checkSum *= 3;
      for (int i = 0; i < 12; i = i + 2) {
        checkSum += Character.getNumericValue(isn.charAt(i));
      }
      checkSum = (10 - (checkSum % 10));
      if (checkSum > 9) {
        checkSum = 0;
      } else {
        checkSum = checkSum;
      }
      return (char) (checkSum + 48);
    } else {
      return generateCheckIsbn10Issn(isn, ((isn.length() + 1) / 2) * 2);
    }
  }

  /**
   * @param isn the isn to get check digit for
   * @param length - the target length for the supplied isn
   * @return the character corresponding to the check digit (0-9 or 'X')
   */
  private static char generateCheckIsbn10Issn(final String isn, final int length) {
    int checkSum = 0;
    for (int i = 0; i < length - 1; i++) {
      checkSum += Character.getNumericValue(isn.charAt(i)) * (length - i);
    }
    checkSum = (11 - checkSum % 11) % 11;
    if (checkSum == 10) {
      return 'X';
    } else {
      return (char) (checkSum + 48);
    }
  }

  /**
   * @param isbn10 the ISBN to convert
   * @return the ISBN-13 representation of the supplied ISBN-10
   */
  public static String isbn10To13(final String isbn10) {
    return isbn10To13(ISBN_PREFIX1, isbn10);
  }

  public static String isbn10To13(final String prefix, final String isbn10) {
    return prefix + isbn10.substring(0, 9) + generateCheck(prefix + isbn10);
  }

  /**
   * verifies if the given isn string is valid if padded with zeroes to make the correct length.
   *
   * @param isn isn to check
   * @param isnType type of the issn
   * @return true if padded isn validates by check sum digit
   */
  public static boolean lostLeadingZeroes(final String isn, final IdentifierType isnType) {
    switch (isnType) {
      case ISSN:
        return validate(pad(isn, ISSN_LENGTH), ISSN_LENGTH);
      case ISBN10:
        return validate(pad(isn, ISBN10_LENGTH), ISBN10_LENGTH);
      default:
        return false;
    }
  }

  /**
   * takes a 13 digit EAN and returns the ISSN if available.
   *
   * @param ean 13 digit EAN code
   * @return String ISSN if EAN contains, else returns original EAN code
   */
  public static String issnFromEan13(final String ean) {
    if (!(Pattern.matches(EAN13_PATTERN, ean) || ean.substring(0, 3).equals(ISSNEAN13_PREFIX))) {
      return ean;
    }
    return canonicalForm(ean).substring(3, 10) + generateCheck(canonicalForm(ean).substring(3, 10));
  }

  /**
   * @param isn isn to get the type
   * @return IdentifierType from Enum IdentifierType
   * @throws IllegalArgumentException for strings of a length that cannot be an isn and if the check
   *     digit is not valid
   */
  public static IdentifierType getType(final String isn) throws IllegalArgumentException {
    if (!isValidIsn(isn)) {
      throw new IllegalArgumentException("invalid identifier");
    }
    int length = canonicalForm(isn).length();

    if (length == 8) {
      return IdentifierType.ISSN;
    } else if (length == 10) {
      return IdentifierType.ISBN10;
    } else if (isn.substring(0, 3).equals(ISSNEAN13_PREFIX)) {
      return IdentifierType.ISSNEAN13;
    } else if (isn.substring(0, 4).equals(ISMN_PREFIX)) {
      return IdentifierType.ISMN;
    } else if (isn.substring(0, 3).equals(ISBN_PREFIX1)
        || isn.substring(0, 3).equals(ISBN_PREFIX2)) {
      return IdentifierType.ISBN13;
    } else {
      return IdentifierType.OTHER;
    }
  }

  public enum IdentifierType {
    ISSN,
    ISBN10,
    ISBN13,
    ISMN,
    ISSNEAN13,
    OTHER
  }
}
