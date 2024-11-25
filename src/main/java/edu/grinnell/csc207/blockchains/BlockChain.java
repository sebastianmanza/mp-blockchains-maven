package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A full blockchain.
 *
 * @author Sebastian Manza
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The head of the BlockChain
   */
  Node head;
  /**
   * The end of the BlockChain
   */
  Node tail;
  /**
   * The current size of the blockchain
   */
  int size;
  /**
   * How to check if the blockchain is valid.
   */
  HashValidator check;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *              The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    byte[] arr = new byte[0];
    Hash prevHash = new Hash(arr);
    Transaction transaction = new Transaction("", "", 0);
    Block newBlock = new Block(0, transaction, prevHash, check);
    this.head = new Node(newBlock);
    this.tail = this.head;
    this.size = 1;
    this.check = check;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *          The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    Block addBlock = new Block(this.size, t, this.tail.getBlock().getHash(), this.check);
    return addBlock;
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *            The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *                                  the hash is not appropriate for the
   *                                  contents, or (c) the previous
   *                                  hash is incorrect.
   */
  public void append(Block blk) throws IllegalArgumentException{
    /* Check for invalid contents. */
    if (!check.isValid(blk.getHash())) {
      throw new IllegalArgumentException("Hash is not valid.");
    } //if

    if (!blk.getHash().equals(blk.computeHash())) {
      throw new IllegalArgumentException("Hash is not appropriate for its contents.");
    } //if

    if (!blk.getPrevHash().equals(this.tail.getBlock().getHash())) {
      throw new IllegalArgumentException("Previous hash is incorrect.");
    } //if

    /* Create a new node and add it to the end of the chain. */
    Node newBlock = new Node(blk);
    this.tail.next = newBlock;
    newBlock.prev = this.tail;
    this.tail = newBlock;
    this.size++;
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *         not removed) or true otherwise (in which case the last block
   *         is removed).
   */
  public boolean removeLast() {
    if (this.size == 1) {
      return false;
    } else {
      this.tail = this.tail.prev;
      this.tail.next = null;
      this.size--;
    }
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.tail.getBlock().getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    Node node = this.head;
    UserDataBase userDataBase = new UserDataBase(new UserDataBaseNode(""));
    while(!node.equals(this.tail)) {
      /* Attempt to add the new transaction to the database, if it returns false
       * it is an invalid transaction.
       */
      if (!userDataBase.updateDataBase(node.getBlock().getTransaction())) {
        return false;
      }
      /* If the previous hash doesn't equal what we would expect. */
      if(!node.equals(this.head)) {
        if (!node.getBlock().prevHash.equals(node.prev.getBlock().getHash())) {
          return false;
        } //if
      } //if

      /* If the current hash doesn't equal what we'd expect. */
      if (!node.getBlock().getHash().equals(node.getBlock().computeHash())) {
        return false;
      } //if
      /* If the hash is not valid. */
      if (!check.isValid(node.getBlock().getHash())) {
        return false;
      } //if
      node = node.next;
    } //while
    return true;
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *                   If things are wrong at any block.
   */
  public void check() throws Exception {
    if (!this.isCorrect()) {
      throw new Exception("The blocks are incorrect.");
    } //if
  } //check()


  /**
   * Find one user's balance.
   *
   * @param user
   *             The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    UserDataBase dataBase = new UserDataBase(new UserDataBaseNode(""));
    Node curNode = this.head;
    while(curNode.next != null) {
      dataBase.updateDataBase(curNode.getBlock().getTransaction());
      curNode = curNode.next;
    } //while
    UserDataBaseNode node = dataBase.findUser(user);
    return node.getUserBalance();
  } // balance()

  /**
   * Get an iterator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      public boolean hasNext() {
        return ;
      } // hasNext()

      public Block next() {
        throw new NoSuchElementException(); // STUB
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    
    return new Iterator<Transaction>() {
      public boolean hasNext() {
        return false; // STUB
      } // hasNext()

      public Transaction next() {
        throw new NoSuchElementException(); // STUB
      } // next()
    };
  } // iterator()

} // class BlockChain
