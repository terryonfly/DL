package com.robot.split.model;

/**
 * Created by terry on 15/9/21.
 */
public class Phrase {

    String word_A_string;
    String word_B_string;
    double phrase_probability = 0.0;
    public int uncommit_feedback_times = 0;

    public Phrase(String a_word_A_string, String a_word_B_string) {
        word_A_string = a_word_A_string;
        word_B_string = a_word_B_string;
    }

    public Phrase(Word a_word_A, Word a_word_B) {
        word_A_string = a_word_A.to_string();
        word_B_string = a_word_B.to_string();
    }

    public Phrase(String a_word_A_string, String a_word_B_string, double a_phrase_probability) {
        word_A_string = a_word_A_string;
        word_B_string = a_word_B_string;
        phrase_probability = a_phrase_probability;
    }

    public String to_string() {
        String to_string = "";
        to_string += word_A_string;
        to_string += "-";
        to_string += word_B_string;
        return to_string;
    }

    public double get_phrase_probability() {
        return phrase_probability;
    }

    public void set_phrase_probability(double a_phrase_probability) {
        phrase_probability = a_phrase_probability;
    }
}
