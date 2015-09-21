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
            if (conn.isClosed()) {
                System.out.println("Failure connecting to the Database");
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
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
        return word;
    }
	
    public ArrayList<Word> pre_load_words(String table, int page, int page_size) {
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
            statement.close();
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
            String sql = "insert into `corpus`.`word` values(NULL, '" + a_word.to_string() + "', '" + a_word.get_word_type() + "', " + a_word.uncommit_feedback_times + ", 0.00000000000000000000001) on duplicate key update word.repeat = (word.repeat+" + a_word.uncommit_feedback_times + ");";
            int res = statement.executeUpdate(sql);
            if (res > 0) {
//                System.out.println("Inser or update successed : " + res);
            } else if (res <= 0) {
//                System.out.println("Inser or update failed");
            } else {
//                System.out.println("Inser or update too many items : " + res);
            }
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }
}
