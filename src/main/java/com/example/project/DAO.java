package com.example.project;

import java.sql.*;
import java.util.ArrayList;

public class DAO {

    //create a connection
    Connection connection;

    //connect to the database
    DAO() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:Quiz.db");
    }

    //get the categories from the database
    public ArrayList<String> getCategories() throws SQLException {
        //write the sql query to extract category information from the database
        PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT Category FROM Quiz");
        //create an array list to store categories
        ArrayList<String> al = new ArrayList<>();
        //create a result set to hold info from the sql statement
        ResultSet rs = ps.executeQuery();
        //loop through result set and add results to the array
        while(rs.next()){
            al.add(rs.getString(1));
        }
        return al;
    }


    public ArrayList<Questions> getQuestions(String category) throws SQLException {
        //create an array list to store questions
        ArrayList<Questions> al = new ArrayList<>();
        //write the sql query to extract question information from the database
        PreparedStatement ps = connection.prepareStatement("SELECT *  FROM Quiz WHERE category = ?");

        // set the category in the sql query to be what the user selected
        ps.setString(1, category);

        //create a result set to hold info from the sql statement
        ResultSet rs = ps.executeQuery();

        // get the question, and answer choices/ correct answer from the sql query.
        while(rs.next()){
            al.add(new Questions(rs.getString(2),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)));
        }
        return al;
    }


    public ArrayList<Scorecard> getScorecards() throws SQLException {
        //new array for the table with best scores
        ArrayList<Scorecard> al = new ArrayList<>();
        //create sql statement to retrieve scores from database
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Scores");
        //create a result set to hold info from the sql statement
        ResultSet rs = ps.executeQuery();
        //while there are results
        while(rs.next()){
            // retrieve that quiz and score from the database
            al.add(new Scorecard( rs.getString(1), rs.getString(2)));
        }
        return al;
    }

    public void update(String category, String newScore) throws SQLException {
        //update scores if new score is better than the old score
        PreparedStatement ps = connection.prepareStatement("UPDATE Scores SET Score = ? WHERE Quiz = ?");
        ps.setString(1, newScore);
        ps.setString(2, category);
        ps.executeUpdate();
    }

    // method to reset the values in the score table/ database
    public void reset() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE Scores SET Score = ?");
        ps.setString(1, "N/A");

        ps.executeUpdate();
    }
}
