package com.example.project;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleClient1 implements Runnable{

    //create socket, dao object, and initialize server port at 3000
    Socket socket;
    DAO dao;
    int serverPort = 3000;

    //call constructor to initialize socket and dao
    HandleClient1(Socket socket) throws SQLException {
        this.socket = socket;
        dao = new DAO();
    }

    @Override
    public void run() {
        try{

            // Printwriter and BufferedReader to handle in the input from the Client, and send it back to the Client
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //create a socket to send to and receive messages from the server
            DatagramSocket clientSocket = new DatagramSocket();
            //retrieve server's ip address
            InetAddress serverAddress = InetAddress.getByName("localhost");

            //read in category selected by the user
            String category = in.readLine();
            // arraylist that stores the questions for the selected quiz
            ArrayList<Questions> al = dao.getQuestions(category);
            // quizInfo is a string that contains the questions and answer choices to send back to the client
            String quizInfo = "";
            // rightAnswer is a string that stores all the correct answers to server 2 to be graded
            String rightAnswer = "";

            //adding question information to the array and separating by the !@
            for(int i=0; i< al.size(); i++){
                quizInfo += al.get(i).question + "!@";
                quizInfo += al.get(i).answer1 + "!@";
                quizInfo += al.get(i).answer2 + "!@";
                quizInfo += al.get(i).answer3 + "!@";
                quizInfo += al.get(i).answer4 + "!@";
                rightAnswer += al.get(i).rightAnswer + "!@";
            }

            // send the message back to the client
            out.println(quizInfo);

            //Constructs a datagram packet for sending packets of length to the specified port number on the specified host
            DatagramPacket sndPacket = new DatagramPacket(rightAnswer.getBytes(), rightAnswer.getBytes().length, serverAddress, serverPort);
            //Sends a datagram packet from this socket.
            clientSocket.send(sndPacket);

        } catch(Exception e) {

        }
    }
}
