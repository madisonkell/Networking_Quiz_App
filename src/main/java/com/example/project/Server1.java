package com.example.project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


// multithreaded TCP server
public class Server1 {
    public static void main(String[] args) throws IOException, SQLException {

        // open a socket on port 8000
        ServerSocket s = new ServerSocket(8000);

        while(true) {
            Socket socket = s.accept();
            HandleClient1 c = new HandleClient1(socket);
            // start a new thread
            Thread t = new Thread(c);
            t.start();
        }
    }
}
