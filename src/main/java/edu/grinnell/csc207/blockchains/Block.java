package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Sebastian Manza
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The number of the block. */
  int num;

  /**
   * The transaction.
   */
  Transaction transaction;

  /**
   * The hash of the previous block.
   */
  Hash prevHash;
  /**
   * The nonce.
   */
  long nonce;

  /**
   * The hash of the block.
   */
  Hash hash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *                    The number of the block.
   * @param transaction
   *                    The transaction for the block.
   * @param prevHash
   *                    The hash of the previous block.
   * @param check
   *                    The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    try {
      this.nonce = mine(num, prevHash, transaction, check);
    } catch (Exception NoSuchAlgorithmException) {
    } //try/catch
    this.hash = this.computeHash();

    /* Mine for the nonce */

  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *                    The number of the block.
   * @param transaction
   *                    The transaction for the block.
   * @param prevHash
   *                    The hash of the previous block.
   * @param nonce
   *                    The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.nonce = nonce;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;
    this.hash = this.computeHash();

  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  Hash computeHash() {
    try {
      MessageDigest md = MessageDigest.getInstance("sha-256");
      md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.num).array());
      md.update(this.prevHash.getBytes());
      md.update(this.transaction.getSource().getBytes());
      md.update(this.transaction.getTarget().getBytes());
      md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.transaction.getAmount()).array());
      md.update(ByteBuffer.allocate(Integer.BYTES).putLong(this.nonce).array());

      Hash hash = new Hash(md.digest());
    } catch (Exception e) {
    } // try/catch
    return hash;
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.num;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.hash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    String returnStr = "Transaction: [Source: " + transaction.getSource() + ", Target: " + transaction.getTarget()
        + "Amount: " + transaction.getAmount() +
        "], Nonce: " + this.nonce + ", prevHash: " + this.prevHash + "hash: " + this.hash;
    return returnStr;
  } // toString()

  long mine(int blockNum, Hash previousHash, Transaction transaction, HashValidator valid)
      throws NoSuchAlgorithmException {
    Random rand = new Random();
    while (true) {
      /* Create a new random nonce and hash everything */
      long nonce = rand.nextLong();
      MessageDigest md = MessageDigest.getInstance("sha-256");
      md.update(ByteBuffer.allocate(Integer.BYTES).putInt(blockNum).array());
      md.update(previousHash.getBytes());
      md.update(transaction.getSource().getBytes());
      md.update(transaction.getTarget().getBytes());
      md.update(ByteBuffer.allocate(Integer.BYTES).putInt(transaction.getAmount()).array());
      md.update(ByteBuffer.allocate(Long.BYTES).putLong(nonce).array());

      Hash hash = new Hash(md.digest());

      if (valid.isValid(hash)) {
        break;
      } // if

    }
    return nonce;
  }
} // class Block
