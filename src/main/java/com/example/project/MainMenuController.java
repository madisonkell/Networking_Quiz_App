package com.example.project;
import java.io.*;
import java.net.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainMenuController {

    //
    ObservableList<String> categories;
    //
    static String[] answers;
    static Questions[] quiz;
    //
    static String category;
    Scorecard scorecard;
    ArrayList<Scorecard> sc;
    //
    DAO dao;

    @FXML
    private TableView<Scorecard> tblScores;
    @FXML
    private TableColumn<Scorecard, String> colQuiz;
    @FXML
    private TableColumn<Scorecard, String> colBestScore;
    @FXML
    private Button btnSelect;
    @FXML
    private ListView<String> lstScore;
    @FXML
    private ComboBox<String> cbQuiz;


    public void initialize() throws SQLException {
        //initialize dao
        dao = new DAO();
        //initialize the categories from the database
        categories = FXCollections.observableArrayList(dao.getCategories());
        // sc is an arraylist that stores all the scorecards (high scores for each quiz)
        sc = new ArrayList<>();

        // get the current high scores and put them in the tableview
        sc = dao.getScorecards();
        cbQuiz.setItems(categories);
        cbQuiz.setValue("Sports");
        tblScores.setItems(FXCollections.observableList(sc));

        //match the columns from the tableview with the variables from the Scorecard class
        colQuiz.setCellValueFactory(new PropertyValueFactory<>("quiz"));
        colBestScore.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    @FXML
    void selectQuiz(ActionEvent event) throws IOException, SQLException {
        try {
            String serverResponse;
            //new socket at port 8000, used to communicate with server 1
            Socket socket = new Socket("localhost", 8000);
            //returns an output stream for this socket.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ///returns an input stream for this socket.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //create a socket to send to and receive messages from the server
            DatagramSocket clientSocket = new DatagramSocket();
            //initialize server address
            InetAddress serverAddress = InetAddress.getByName("localhost");

            byte[] buf = new byte[65535];
            DatagramPacket sndPacket, rcvPacket;
            String rcvMessage;
            //port to communicate with server 2
            int serverPort = 3001;

            // get the current selected category from the combobox(user selection) and send it to server
            category = cbQuiz.getSelectionModel().getSelectedItem();
            out.println(category);

            //server1 sends back the questions and possible answers
            serverResponse = in.readLine();

            //split the string at !@ because any other character could show up in a question
            String[] split = serverResponse.split("!@");
            //make an array to store the questions
            quiz = new Questions[split.length];

            // store them as new questions so it's easier to iterate over.
            for (int i = 0; i < split.length / 5; i++) {
                quiz[i] = new Questions(split[i * 5], split[i * 5 + 1], split[i * 5 + 2], split[i * 5 + 3], split[i * 5 + 4]);
            }

            // open the quiz page
            TakeQuiz quiz = launchQuiz();

            // send is a string that is used to store the users answers when taking the quiz
            String send = "";

            // concat the answers with !@
            for (int i = 0; i < answers.length / 5; i++) {
                send += answers[i] + "!@";
            }

            //send the answers to Server 2 to be graded
            sndPacket = new DatagramPacket(send.getBytes(), send.getBytes().length, serverAddress, serverPort);//Constructs a datagram packet for sending packets of length length to the specified port number on the specified host.
            clientSocket.send(sndPacket);//Sends a datagram packet from this socket.

            //Constructs a DatagramPacket for receiving packets of length length.
            rcvPacket = new DatagramPacket(buf, buf.length);
            //Receives a datagram packet from this socket.
            clientSocket.receive(rcvPacket);
            // rcvPacket.getData() returns the entire buffer buf (which we initially created of size 1000) includes both the server information and leftover bytes
            // rcvPacket.getLength() returns the actual size of data sent by the server
            // get the grade back from server 2
            rcvMessage = new String(rcvPacket.getData(), 0, rcvPacket.getLength());


            // check the current high score against the new score
            for (int i = 0; i < sc.size(); i++) {
                if (sc.get(i).quiz.equals(category)) {
                    //check if there is a score
                    if (!sc.get(i).score.equals("N/A")) {
                        // if the new score is higher than the previous, update the database
                        if (Integer.parseInt(sc.get(i).getScore()) < Integer.parseInt((rcvMessage)) * 20) {
                            dao.update(category, String.valueOf(Integer.parseInt(rcvMessage) * 20));
                        }
                        // if there isnt a previous score, this is the new high score
                    } else {
                        dao.update(category, String.valueOf(Integer.parseInt(rcvMessage) * 20));
                    }
                }
            }

            // update the scorecards
            sc = dao.getScorecards();
            tblScores.setItems(FXCollections.observableList(sc));
            tblScores.refresh();

            //test to see if you need
            send = "";
        }
        catch (Exception e){

        }
    }
// method to reset the scores in the database (button click)
    @FXML
    void resetScore(ActionEvent event) throws SQLException {
        dao.reset();
        sc = dao.getScorecards();
        tblScores.setItems(FXCollections.observableList(sc));
        tblScores.refresh();
    }


    //launch the quiz page
    @FXML
    public TakeQuiz launchQuiz() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(("take-quiz.fxml")));
        Scene scene = new Scene(loader.load(),446,440);
        Stage newWindow = new Stage();
        newWindow.setScene(scene);
        newWindow.setTitle("Quiz");
        newWindow.showAndWait();
        return loader.getController();
    }

    //getters for the quiz and category
    public Questions[] getQuiz(){
        return quiz;
    }

    public String getCategory(){
        return  category;
    }

    }


