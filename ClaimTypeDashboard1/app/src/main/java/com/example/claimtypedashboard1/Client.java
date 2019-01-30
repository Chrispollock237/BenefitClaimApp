package com.example.claimtypedashboard1;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Client implements Serializable {

    // Member variables
    private String firstName;
    private String lastName;
    private String userName;
    private String userPassword;
    private int clientIdNum;

    /**
     * Constructor Method
     */
    public Client () {}

    /** Constructor method
     * @param fn The first name of the client
     * @param ln The last name of the client
     * @param un The user name of the client
     * @param up The password of the client
     * @param id The client id number
     */
    Client (String fn, String ln, String un, String up, int id) {
        this.firstName = fn;
        this.lastName = ln;
        this.userName = un;
        this.userPassword = up;
        this.clientIdNum = id;
    }

    /**
     * Copy Constructor method
     * @param c client object
     */
    Client (Client c) {
        this.firstName = c.firstName;
        this.lastName = c.lastName;
        this.userName = c.userName;
        this.userPassword = c.userPassword;
        this.clientIdNum = c.clientIdNum;
    }

    /**
     * Get a client object
     * @return A client object
     */
    public Client getClient() {
        Client client = new Client();
        return client;
    }

    // Assignment operator???

    /**
     * Gets the first name of the client
     * @return The first name of the client.
     */
    String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the client
     * @return The last name of the client.
     */
    String getLastName() {
        return lastName;
    }

    /**
     * Gets the user name of the client
     * @return The first name of the client.
     */
    String getUserName() {
        return userName;
    }

    /**
     * Gets the user name of the client
     * @return The first name of the client.
     */
    String getUserPassword() {
        return userPassword;
    }

    /**
     * Gets the id number of the client
     * @return The id number of the client.
     */
    int getId() {
        return clientIdNum;
    }
}
