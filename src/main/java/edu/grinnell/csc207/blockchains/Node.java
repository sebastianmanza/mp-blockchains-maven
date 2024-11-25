package edu.grinnell.csc207.blockchains;

/**
 * A basic node containing a block of a blockchain.
 * @author Sebastian Manza
 */
public class Node {
   
    /**
     * The next block in the chain.
     */
    Node next;

    /**
     * The current block value.
     */
    Block current;

    /**
     * The previous block in the chain.
     */
    Node prev;



    /**
     * Build a new Node with a block
     * @param block the block to create the node with
     */
    public Node(Block block) {
        this.current = block;
        this.next = null;
        this.prev = null;
    } //Node(Block)

    /**
     * Get the block of the node
     * @return the Block at that node
     */
    public Block getBlock() {
        return this.current;
    } //getBlock()
}
