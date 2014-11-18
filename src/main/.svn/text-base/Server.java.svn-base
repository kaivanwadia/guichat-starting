package main;

import java.io.IOException;

import Server.ServerModel;

/**
 * Chat server runner.
 */
public class Server {
    public static final int PORT_NUMBER = 4444;

    /**
     * Start a chat server.
     */
    public static void main(String[] args) {
        int port = PORT_NUMBER;
        if (args.length==1) {
            port = Integer.parseInt(args[0]);
        }
        try {
            ServerModel server = new ServerModel(port);
            System.out.println("Server is up an running");
            server.serve();
        } catch (IOException e) {
            System.err.println("Error in creating a server");
            e.printStackTrace();
        }
    }
}
