package com.robot.database;

import java.sql.*;

import com.robot.runtime.info.RuntimeInfo;
import com.robot.split.model.*;

import java.util.ArrayList;

public class Connector {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost/corpus?Unicode=true&characterEncoding=UTF8";
//    String url = "jdbc:mysql://robot.mokfc.com/corpus?Unicode=true&characterEncoding=UTF8";
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

    public ArrayList<Phrase> pre_load_phrases(String table, int page, int page_size) {
        ArrayList<Phrase> phrases = new ArrayList<Phrase>();
        try {
            if(!is_connected() && !connect())
                return phrases;
            Statement statement = conn.createStatement();
            String sql = "select * from " + table + " where " + table + ".id > " + page + " limit " + page_size + ";";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String phrase_string = rs.getString("phrase");
                double phrase_probability = rs.getDouble("probability");
                Phrase phrase = new Phrase(phrase_string, phrase_probability);
                phrases.add(phrase);
            }
            rs.close();
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return phrases;
        }
        return phrases;
    }

    public void feedback_word(Word a_word) {
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "insert into `corpus`.`word` values(NULL, '" + a_word.to_string() + "', '" + a_word.get_word_type() + "', " + a_word.uncommit_feedback_times + ", 0.00000000000000000000001) on duplicate key update word.repeat = (word.repeat+" + a_word.uncommit_feedback_times + ");";
            int res = statement.executeUpdate(sql);
            if (res > 0) {
//                System.out.println("Inser or update word successed : " + res);
            } else if (res <= 0) {
//                System.out.println("Inser or update word failed");
            } else {
//                System.out.println("Inser or update word too many items : " + res);
            }
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }
    }

    public void feedback_phrase(Phrase a_phrase) {
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "insert into `corpus`.`phrase` values(NULL, '" + a_phrase.to_string() + "', " + a_phrase.uncommit_feedback_times + ", 0.00000000000000000000001) on duplicate key update phrase.repeat = (phrase.repeat+" + a_phrase.uncommit_feedback_times + ");";
            int res = statement.executeUpdate(sql);
            if (res > 0) {
//                System.out.println("Inser or update phrase successed : " + res);
            } else if (res <= 0) {
//                System.out.println("Inser or update phrase failed");
            } else {
//                System.out.println("Inser or update phrase too many items : " + res);
            }
            statement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }
    }

    public void add_url(String a_url) {
        if (a_url.length() > 250) {
            System.err.printf("New url's length > 250 and pass\n");
            return;
        }
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "insert into `corpus`.`urls` values(NULL, '" + a_url + "', 0) on duplicate key update urls.getted = urls.getted;";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e1) {
//            System.err.printf("add_url err : %s\n", e1);
//            e1.printStackTrace();
            return;
        }
    }

    public ArrayList<String> get_urls(int count) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            if(!is_connected() && !connect())
                return urls;
            Statement statement = conn.createStatement();
            String sql = "select * from urls where urls.getted = 0 order by rand() limit " + count + ";";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int url_id = rs.getInt("id");
                urls.add(rs.getString("url"));
                set_url_getted(url_id);
            }
            rs.close();
            statement.close();
        } catch (SQLException e1) {
//            e1.printStackTrace();
            return urls;
        }
        return urls;
    }

    public void set_url_getted(int a_url_id) {
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "update corpus.urls set getted=1 where id=" + a_url_id + ";";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e1) {
//            e1.printStackTrace();
            return;
        }
    }

    public void add_web_content(String a_web_content) {
        try {
            if(!is_connected() && !connect())
                return;
            a_web_content = a_web_content.replaceAll("'", "\\'");
            Statement statement = conn.createStatement();
            String sql = "insert into `corpus`.`web_content` values (NULL, '" + a_web_content + "', 0);";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e1) {
//            System.err.printf("add_web_content err : %s\n", e1);
//            e1.printStackTrace();
            return;
        }
    }

    public String get_web_content() {
        String web_content = "";
        try {
            if(!is_connected() && !connect())
                return web_content;
            Statement statement = conn.createStatement();
            String sql = "select * from web_content where web_content.has_split = 0 limit 5000;";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int web_content_id = rs.getInt("id");
                web_content += rs.getString("web_content");
		web_content += "\n";
                set_web_content_getted(web_content_id);
            }
            rs.close();
            statement.close();
        } catch (SQLException e1) {
//            e1.printStackTrace();
            return web_content;
        }
        return web_content;
    }

    public void set_web_content_getted(int a_web_content_id) {
        try {
            if(!is_connected() && !connect())
                return;
            Statement statement = conn.createStatement();
            String sql = "update corpus.web_content set has_split=1 where id=" + a_web_content_id + ";";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e1) {
//            e1.printStackTrace();
            return;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }
}
