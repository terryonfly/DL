package com.robot.split.splitor;

import com.robot.database.Connector;
import com.robot.split.cache.CorpusWords;
import com.robot.split.cache.CorpusPhrase;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;

import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class WordSplitor {

    Connector db;
    CorpusWords cache_words_unnormal;
    CorpusWords cache_chars_unnormal;
    CorpusPhrase cache_phrase_unnormal;
    WordFeedback wordFeedback;
    PhraseFeedback phraseFeedback;

    public WordSplitor() {
        db = new Connector();
        db.connect();
        cache_words_unnormal = new CorpusWords("word_unnormal");
        cache_chars_unnormal = new CorpusWords("char_unnormal");
        wordFeedback = new WordFeedback("Word Feedback");
        phraseFeedback = new PhraseFeedback("Phrase Feedback");
        wordFeedback.start();
        phraseFeedback.start();
    }

    public boolean isMessyCode(String a_string_sentence) {
        if (a_string_sentence.length() == 0) return false;
        float messy_count = 0;
        for (int i = 0; i < a_string_sentence.length(); i ++) {
            String string_word = a_string_sentence.substring(i, i + 1);
            Word word = cache_chars_unnormal.search_from_cache(string_word);
            messy_count += (word == null) ? 1 : 0;
        }
        if (messy_count / (float)a_string_sentence.length() > 0.4) {
//            System.err.printf("%s\n", a_string_sentence);
            return true;
        }
        return false;
    }

    public Sentence split_word(String a_string_sentence) {
//        System.out.println("Checking : " + a_string_sentence);
        ArrayList<Sentence> sentences = get_posible_sentences(a_string_sentence);
//        System.out.println("Posible count : " + sentences.size());
        Sentence best_sentence = choose_best_sentence(sentences);
        sentences.clear();
        wordFeedback.feedback_sentence(best_sentence);
        phraseFeedback.feedback_sentence(best_sentence);
        return best_sentence;
    }

    private ArrayList<Sentence> get_posible_sentences(String a_string_sentence) {
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        for (int len = (a_string_sentence.length() > 10) ? 10 : a_string_sentence.length(); len > 0; len --) {
            String first_string_word = a_string_sentence.substring(0, 0 + len);
            Word first_word = null;
            if (first_word == null) {
                first_word = cache_words_unnormal.search_from_cache(first_string_word);
            }
            if (first_word == null) {
                if (first_word == null && len == 1) {
                    first_word = cache_chars_unnormal.search_from_cache(first_string_word);
                    if (first_word != null) {
                        double probability = first_word.get_word_probability();
                        probability *= 0.1;
                        first_word.set_word_probability(probability);
                    }
                }
                if (first_word == null && len == 1) {
                    first_word = new Word(first_string_word);
                }
                if (first_word != null) cache_words_unnormal.add_to_cache(first_string_word, first_word);
            }
            if (first_word != null) {
                if (a_string_sentence.length() == len) {// Im the last word
                    Sentence parent_sentence = new Sentence();
                    parent_sentence.add_word(first_word);
                    sentences.add(parent_sentence);
                } else {
                    String sub_string_sentence = a_string_sentence.substring(len);
                    ArrayList<Sentence> sub_sentences = get_posible_sentences(sub_string_sentence);
                    for (int i = 0; i < sub_sentences.size(); i ++) {
                        Sentence parent_sentence = new Sentence();
                        parent_sentence.add_word(first_word);
                        parent_sentence.add_sentence(sub_sentences.get(i));
                        sentences.add(parent_sentence);
                    }
                    sub_sentences.clear();
                }
            }
        }
        return sentences;
    }

    private Sentence choose_best_sentence(ArrayList<Sentence> sentences) {
        Sentence best_sentence = new Sentence();
        if (sentences.size() == 0) return best_sentence;
        for (int i = 0; i < sentences.size(); i ++) {
            if (best_sentence.length() == 0) {
                best_sentence = sentences.get(i);
            } else {
                if (best_sentence.get_score() < sentences.get(i).get_score()) {
                    best_sentence = sentences.get(i);
                }
            }
        }
        return best_sentence;
    }

    @Override
    protected void finalize() throws Throwable {
        wordFeedback.stop();
        phraseFeedback.stop();
        super.finalize();
    }
}
