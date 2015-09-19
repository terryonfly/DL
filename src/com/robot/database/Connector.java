package com.robot.database;

import java.sql.*;
import com.robot.split.model.*;

import java.util.ArrayList;

public class Connector {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://robot.mokfc.com/corpus?Unicode=true&characterEncoding=UTF8";
    String user = "root";
    String password = "513939";

    Connection conn = null;

    public boolean connect() {
        try {
            Class.forName(driver);
            if (conn != null) {
                if (!conn.isClosed()) {
                    conn.close();
                }
                conn = null;
            }
            conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed()) {
                System.out.println("Succeeded connecting to the Database");
            } else {
                return false;
            }
        } catch(ClassNotFoundException e) {
            System.out.println("Sorry, can`t find the Driver");
            e.printStackTrace();
            return false;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            conn = null;
        }
    }

    public boolean is_connected() {
        if (conn == null) {
            return false;
        }
        try {
            return !conn.isClosed();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public Word check_word(String check_word, String table) {
        Word word = null;
        try {
            System.out.print(".");
            if(!is_connected() && !connect())
                return null;
            Statement statement = conn.createStatement();
            String sql = "select * from " + table + " where " + table + ".word = '" + check_word + "' limit 1;";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                String word_string = rs.getString("word");
                String word_type = rs.getString("type");
                double word_probability = rs.getDouble("probability");
                word = new Word(word_string, word_type, word_probability);
            }
            rs.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
        return word;
    }
	
    public ArrayList<Word> preload_words(String table, int page, int page_size) {
        ArrayList<Word> words = new ArrayList<Word>();
        try {
            if(!is_connected() && !connect())
                return words;
            Statement statement = conn.createStatement();
            String sql = "select * from " + table + " where " + table + ".id > " + page + " limit " + page_size + ";";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String word_string = rs.getString("word");
                String word_type = rs.getString("type");
                double word_probability = rs.getDouble("probability");
                Word word = new Word(word_string, word_type, word_probability);
				words.add(word);
            }
            rs.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return words;
        }
        return words;
    }

    public void feedback_word(Word a_word) {
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "select * from word where word.word = '" + a_word.to_string() + "' limit 1;";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {// Update
                sql = "update word set word.repeat = (word.repeat+1) where word='" + a_word.to_string() + "' limit 1;";
                System.out.print("Update " + a_word.to_string() + " ---- ");
                int res = statement.executeUpdate(sql);
                if (res != -1) {
                    System.out.println("Successed");
                } else {
                    System.out.println("Failed");
                }
            } else {// Insert
                sql = "insert into `corpus`.`word` values(NULL, '" + a_word.to_string() + "', '" + a_word.get_word_type() + "', 1, 0.00000000000000000000001);";
                System.out.print("Insert " + a_word.to_string() + " ---- ");
                int res = statement.executeUpdate(sql);
                if (res != -1) {
                    System.out.println("Successed");
                } else {
                    System.out.println("Failed");
                }
            }
            rs.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }
    }
}
