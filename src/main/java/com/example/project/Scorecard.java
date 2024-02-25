package com.example.project;

public class Scorecard {

    static int size;
    public String quiz;
    public String score;

    //constructor passing the quiz and score in
    public Scorecard(String quiz, String score){
        this.quiz = quiz;
        this.score = score;
        size++;
    }

    //getters for the score, size, and quiz: need for information generation
    public int getSize() {
        return size;
    }
    public String getQuiz() {
        return quiz;
    }
    public String getScore() {
        return score;
    }
}
