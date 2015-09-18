package com.robot.split.splitor;

import com.robot.database.Connector;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;

import java.util.HashMap;

/**
 * Created by terry on 15/9/19.
 */
public class Feedback {

    Connector db;

    public Feedback() {
        db = new Connector();
        db.connect();
    }

    public void feedback_sentence(Sentence a_sentence) {
        for (int i = 0; i < a_sentence.length(); i ++) {
            feedback_word(a_sentence.at(i));
        }
    }

    public void feedback_word(Word a_word) {
        db.feedback_word(a_word);
    }
}
