package com.robot.split.model;

/**
 * Created by terry on 15/9/18.
 */
public class Word {

    String word_string;
    String word_type = "";
    double word_probability = 0.0;
    boolean is_new_word = true;

    public Word(String a_new_word) {
        word_string = a_new_word;
    }

    public Word(String a_new_word, String a_word_type, double a_word_probability) {
        word_string = a_new_word;
        word_type = a_word_type;
        word_probability = a_word_probability;
        is_new_word = false;
    }

    public int length() {
        return word_string.length();
    }

    public String to_string() {
        return word_string;
    }

    public String get_word_type() {
        return word_type;
    }

    public double get_word_probability() {
        return word_probability;
    }

    public void set_word_probability(double a_word_probability) {
        word_probability = a_word_probability;
    }
}
