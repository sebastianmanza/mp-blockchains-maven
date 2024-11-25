package edu.grinnell.csc207.blockchains;

/**
 * A basic class that stores a user and balance in a singly linked list.
 * @author Sebastian Manza
 */
public class UserDataBaseNode {
    String userName;
    int userBalance;
    UserDataBaseNode nextUser;

    /**
     * Creates a UserDataBaseNode that stores a user and balance.
     *
     * @param user The username
     */
    public UserDataBaseNode(String user) {
        this.userName = user;
        this.userBalance = 0;
        this.nextUser = null;
    }

    /**
     * Changes the account balance of a given user.
     *
     * @param amount The amount to add/subtract
     * @return true if the amount could be changed, false if the transaction is
     *         illegal.
     */
    public boolean changeBalance(int amount) {
        if (this.userBalance < (0 - amount)) {
            return false;
        } // if it would cause the user to go negative
        this.userBalance += amount;
        return true;
    } // changeBalance

    /**
     * Get the users name.
     *
     * @return a string representing the users name
     */
    public String getUserName() {
        return this.userName;
    } // userName()

    /**
     * Get the users balance.
     *
     * @return An integer representing the users balance
     */
    public int getUserBalance() {
        return this.userBalance;
    } //getUserBalance
} // class UserDataBaseNode
