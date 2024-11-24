package edu.grinnell.csc207.blockchains;

public class Node {
    
    Node next;

    Block current;



    public Node(Block block) {
        this.current = block;
        this.next = null;
    }

    public Block getBlock() {
        return this.current;
    }
}
