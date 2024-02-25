package com.example.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


// multithreaded UDP server
public class Server2 {

    public static void main(String[] args) throws IOException {

        //create two sockets at different ports and two packets
        DatagramSocket socket1 = new DatagramSocket(3000);
        DatagramSocket socket2 = new DatagramSocket(3001);
        DatagramPacket packet, packet2;

        //create two buffers to send two packets

        //it's better to keep the buffer size with in the MTU limit i.e. 1472 bytes
        byte[] buf = new byte[65535];
        //it's better to keep the buffer size with in the MTU limit i.e. 1472 bytes
        byte[] buf2 = new byte[65535];


        while (true) {
            //initialize packets
            packet = new DatagramPacket(buf, buf.length);
            packet2 = new DatagramPacket(buf2, buf2.length);
            //wait for incoming data from client
            socket1.receive(packet);
            socket2.receive(packet2);
            // start new thread
            HandleClient2 c = new HandleClient2(socket1, socket2, packet, packet2);
            Thread t = new Thread(c);
            t.start();
        }
    }
}
