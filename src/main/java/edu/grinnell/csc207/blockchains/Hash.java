package edu.grinnell.csc207.blockchains;

import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encapsulated hashes.
 *
 * @author Yash Malik
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The data for this hash.
   */
  private final byte[] hashData;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data
   *   The data to copy into the hash.
   */
  public Hash(byte[] data) {
    if (data == null) {
      throw new IllegalArgumentException("Data cannot be null");
    } // obviously
    // Make a deep copy of the data to ensure encapsulation
    this.hashData = Arrays.copyOf(data, data.length);
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return hashData.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i
   *   The index of the byte to get, between 0 (inclusive) and
   *   length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    if (i < 0 || i >= hashData.length) {
      throw new IndexOutOfBoundsException("Index " + i + " out of bounds");
    }  // overly cautious error check
    return hashData[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the client
   * cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    return Arrays.copyOf(hashData, hashData.length);
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    StringBuilder hexString = new StringBuilder();
    for (byte b : hashData) {
      hexString.append(String.format("%02X", b));
    }  // Originally used "%02x" but failed one test and took an hour to debug 
    return hexString.toString();
  } // toString()

  /**
   * Determine if this is equal to another object.
   *
   * @param other
   *   The object to compare to.
   *
   * @return true if the two objects are conceptually equal and false
   *   otherwise.
   */
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }  // obviously
    if (other == null || other.getClass() != this.getClass()) {
      return false;
    }  // hash might match but be for different things
    Hash otherHash = (Hash) other;
    return Arrays.equals(this.hashData, otherHash.hashData);
  } // equals(Object)

  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return Arrays.hashCode(hashData);
  } // hashCode()

  public static Hash computeHash(String blockContents) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(blockContents.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        return new Hash(hashBytes);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("SHA-256 is not on this system", e);
    } // catch RuntimeException
  } // computeHash(String blockContents)
} // class Hash
