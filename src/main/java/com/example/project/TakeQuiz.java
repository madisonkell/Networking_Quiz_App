package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class TakeQuiz {
        //fxml contents of the GUI
        @FXML
        private Button btnNext;
        @FXML
        private Label lblCategory;
        @FXML
        private Label lblQ;
        @FXML
        private RadioButton rdb1;
        @FXML
        private RadioButton rdb2;
        @FXML
        private RadioButton rdb3;
        @FXML
        private RadioButton rdb4;
        @FXML
        private ToggleGroup tgGroup;

        //to determine if the quiz is done
        Boolean done = false;

        //arrays to hold the questions and answers
        Questions[] questions;
        String[] answers;

        int index = 0;
        String category;
        //
        MainMenuController controller;

        public void initialize() throws IOException {
                //calling the fxml file associated with the controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                controller = loader.getController();
                //loading the questions and the category to display
                questions = controller.getQuiz();
                category = controller.category;
                // set the label for quiz type
                lblCategory.setText(category);


                //initialize the toggle group
                tgGroup = new ToggleGroup();
                //add radiobuttons to the toggle group
                rdb1.setToggleGroup(tgGroup);
                rdb2.setToggleGroup(tgGroup);
                rdb3.setToggleGroup(tgGroup);
                rdb4.setToggleGroup(tgGroup);

                //initialize the answers array
                answers = new String[questions.length];

                //dont show the next button until the question is answered
                btnNext.setVisible(false);
                //call the functions that creates the questions
                postQuestions();

                //listen for the change of the toggle group
                tgGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
                        //create a temp variable to keep track of which toggle is selected
                        RadioButton temp = (RadioButton) tgGroup.getSelectedToggle();
                        //if the last radiobutton has been selected
                        if(temp != null){
                                //the quiz is completed (there is no questions left)
                                if(index >= (questions.length/5) -1){
                                        //set the text of the next button to submit, set the boolean to true
                                        btnNext.setText("Submit");
                                        done = true;
                                }
                                //show the button
                                btnNext.setVisible(true);
                        } else {
                                //do not show the button
                                btnNext.setVisible(false);
                        }
                });
        }

        //function that posts the question
        public void postQuestions(){
                //defaults all choices to false (not selected)
                rdb1.setSelected(false);
                rdb2.setSelected(false);
                rdb3.setSelected(false);
                rdb4.setSelected(false);
                //numbering and posting the question with corresponding answers
                lblQ.setText(index+1 + ". " + questions[index].question);
                rdb1.setText(questions[index].getAnswer1());
                rdb2.setText(questions[index].getAnswer2());
                rdb3.setText(questions[index].getAnswer3());
                rdb4.setText(questions[index].getAnswer4());
        }

        //event happens when the next question button is clicked
        @FXML
        void nextQuestion(ActionEvent event) {
                //create a temp variable to keep track of which toggle is selected
                RadioButton temp = (RadioButton) tgGroup.getSelectedToggle();
                //set the user answer to the index of the selected toggle
                answers[index] = temp.getText();
                //increase index
                index++;

                //if quiz is complete
                if(done){
                        //
                        controller.answers = answers;
                        //update the GUI & close it
                        Stage stage = (Stage) btnNext.getScene().getWindow();
                        stage.close();
                }
                else {
                        //post the questions
                        postQuestions();
                }
        }
}