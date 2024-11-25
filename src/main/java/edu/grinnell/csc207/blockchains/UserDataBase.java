package edu.grinnell.csc207.blockchains;

/**
 * A class representing our user database, and account balances.
 *
 * @author Sebastian Manza
 */
public class UserDataBase {
    UserDataBaseNode head;

    /**
     * Creates a new UserDataBase out of a given node
     *
     * @param node The node to be used as the head
     */
    public UserDataBase(UserDataBaseNode node) {
        this.head = node;
    } // UserDataBase

    /**
     * Return false if a transaction is unable to be updated.
     * @param transaction The transaction to update the database with
     * @return true if it was updated, false if unable.
     */
    public boolean updateDataBase(Transaction transaction) {
        UserDataBaseNode src = findUser(transaction.getSource());
        UserDataBaseNode targ = findUser(transaction.getTarget());

        /* If the source was not found and it wasn't an empty string, its invalid */
        if (src == null) {
            return false;
        } //if

        /* If the target wasn't found, we should create a new node. */
        if (targ == null) {
            targ = new UserDataBaseNode(transaction.getTarget());
            targ.nextUser = this.head;
            this.head = targ;
        } //if

        if(!src.userName.equals("")) {
            /* If changeBalance returned false, it was an invalid transaction */
            if (!src.changeBalance(0 - (transaction.getAmount()))) {
                return false;
            } //if
        } //if
        /* Giving someone money should never be invalid */
        targ.changeBalance(transaction.getAmount());
        return true;
    } //updateDataBase

    /**
     * Find a user in the database.
     * @param user the user to find
     * @return the node of the user
     */
    public UserDataBaseNode findUser(String user) {
        UserDataBaseNode curNode = this.head;
        while (curNode.nextUser != null ) {
            if (curNode.userName.equals(user)) {
                return curNode;
            } //if
            curNode = curNode.nextUser;
        } //while
        return null;
    } //findUser

}
