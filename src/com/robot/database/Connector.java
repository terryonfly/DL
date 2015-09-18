package com.robot.database;

import java.sql.*;
import com.robot.split.model.*;

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
}
