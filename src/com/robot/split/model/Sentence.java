package com.robot.split.model;

import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class Sentence {

    ArrayList<Word> words;

    public Sentence() {
        words = new ArrayList<Word>();
    }

    public void add_word(Word a_word) {
        words.add(a_word);
    }

    public void add_sentence(Sentence a_sentence) {
        for (int i = 0; i < a_sentence.length(); i ++) {
            add_word(a_sentence.at(i));
        }
    }

    public Word at(int a_index) {
        if (a_index < length()) {
            return words.get(a_index);
        }
        return null;
    }

    public int length() {
        return words.size();
    }

    public String to_string() {
        String string = "";
        for (int i = 0; i < length(); i ++) {
            string += at(i).to_string();
            string += " ";
        }
        return string;
    }

    public double get_score() {
        double score = 1.0;
        if (length() == 0)
            return 0.0;
        for (int i = 0; i < length(); i ++) {
            if (at(i).word_probability > 0)
                score *= at(i).word_probability;
        }
        return score;
    }
}
