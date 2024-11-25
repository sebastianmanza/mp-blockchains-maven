package edu.grinnell.csc207.blockchains;

public class Node {
    
    Node next;

    Block current;

    Node prev;



    public Node(Block block) {
        this.current = block;
        this.next = null;
        this.prev = null;
    }

    public Block getBlock() {
        return this.current;
    }
}
