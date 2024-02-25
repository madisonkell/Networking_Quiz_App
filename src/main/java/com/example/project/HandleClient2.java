package com.example.project;
import java.net.*;

public class HandleClient2 implements Runnable {

    //create a socket to send to and receive messages from the server through udp
    DatagramSocket s1, s2;
    // client's port number
    int clientPort;
    //it's better to keep the buffer size with in the MTU limit i.e. 1472 bytes
    byte[] buf = new byte[65535];
    //create packets that data will be transferred through
    DatagramPacket sndPacket, rcvPacket,rcvPacket2;
    //retrieve client's ip address
    InetAddress clientAddress;
    //arrays for correct answers and user answers
    String[] answers, userAnswers;

    //takes the DatagramSocket port from the new thread
    public HandleClient2(DatagramSocket s1, DatagramSocket s2, DatagramPacket rcvPacket,DatagramPacket rcvPacket2) throws SocketException {
        this.s1 = s1;
        this.s2 = s2;
        this.rcvPacket = rcvPacket;
        this.rcvPacket2 =rcvPacket2;
    }

    public void run() {
        try {
            //create a counter for the number of items you got correct
            int correct = 0;
            //retrieve client's port number, no need to get servers port because we don't send anything back.
            clientPort = rcvPacket2.getPort();
            //retrieve client's ip address
            clientAddress = rcvPacket2.getAddress();

            //putting the correct answers that were sent into the array and splits it by the !@
            answers = new String(rcvPacket.getData(), 0, rcvPacket.getLength()).split("!@");
            //putting the user answers that were sent into the array and splits it by the !@
            userAnswers = new String(rcvPacket2.getData(), 0, rcvPacket2.getLength()).split("!@");

            //looping through all user answers and increasing the count of the correct answers if it matches the correct answer
            for(int i = 0; i < userAnswers.length; i++){
                if(answers[i].equals(userAnswers[i]))
                    correct ++;
            }

            // send the message back to the client
            //create a packet with the number of correct answers and address it with the client's port number and IP address
            sndPacket = new DatagramPacket(String.valueOf(correct).getBytes(), String.valueOf(correct).getBytes().length, clientAddress, clientPort);
            //send the packet to the client (client details are in the sndPacket) through the serverSocket
            s2.send(sndPacket);
            //reset the buf and continue waiting for new incoming requests
            buf = new byte[1000];

        } catch (Exception e) {

        }
    }
}
