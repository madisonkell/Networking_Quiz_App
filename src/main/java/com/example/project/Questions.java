package com.example.project;

public class Questions {
    String question, answer1, answer2, answer3, answer4, rightAnswer;

    //constructor for the question, user answers, and right answer
    public Questions(String question, String answer1, String answer2, String answer3, String answer4, String rightAnswer){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.rightAnswer = rightAnswer;
    }

    //constructor for the question and the user answers
    public Questions(String question, String answer1, String answer2, String answer3, String answer4){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    //getters for each answer
    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

}
