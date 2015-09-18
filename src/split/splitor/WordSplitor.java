package split.splitor;

import database.Connector;
import split.model.Sentence;
import split.model.Word;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by terry on 15/9/18.
 */
public class WordSplitor {

    Connector db;
    HashMap<String, Word> reuse_words;

    public WordSplitor() {
        db = new Connector();
        db.connect();
        reuse_words = new HashMap<String, Word>();
    }

    public Sentence split_word(String a_string_sentence) {
        System.out.print("Proccessing : ");
        ArrayList<Sentence> sentences = get_posible_sentences(a_string_sentence);
        System.out.println();
        Sentence best_sentence = choose_best_sentence(sentences);
        return best_sentence;
    }

    private ArrayList<Sentence> get_posible_sentences(String a_string_sentence) {
        ArrayList<Sentence> sentences = new ArrayList<Sentence>();
        for (int len = (a_string_sentence.length() > 10) ? 10 : a_string_sentence.length(); len > 0; len --) {
            String first_string_word = a_string_sentence.substring(0, 0 + len);
            Word first_word = null;
            if (first_word == null) {// if true
                first_word = search_word_from_cache(first_string_word);
            }
            if (first_word == null) {// if cache = null
                if (first_word == null) {
                    first_word = db.check_word(first_string_word, "word_sogou");
                }
                if (first_word == null) {// if word_sogou = null
                    first_word = db.check_word(first_string_word, "word_cn");
                } else {
                    if (first_word.to_string().equals("|")) {// if word_sogou has word and has no type
                        System.out.print("finding type : ");
                        Word pre_first_word = db.check_word(first_string_word, "word_cn");
                        if (pre_first_word != null) {
                            System.out.println("found");
                            first_word = pre_first_word;
                        } else {
                            System.out.println("not found");
                        }
                    }
                }
                if (first_word == null && len == 1) {
                    first_word = db.check_word(first_string_word, "char_cn");
                }
                if (first_word == null && len == 1) {
                    first_word = new Word(first_string_word);
                }
                add_word_to_cache(first_string_word, first_word);
            } else {
                if (first_word.length() == 0) {// if cache = ""
                    first_word = null;
                }
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
                }
            }
        }
        return sentences;
    }

    private void add_word_to_cache(String a_string_word, Word a_word) {
        String key = a_string_word;
        if (key == null) return;
        Word value = a_word;
        if (value == null) {
            value = new Word("");
        }
        reuse_words.put(key, value);
    }

    private Word search_word_from_cache(String a_string_word) {
        String key = a_string_word;
        if (key == null) return null;
        Word word = null;
        if (reuse_words.containsKey(key)) {
            System.out.print("*");
            word = reuse_words.get(key);
        }
        return word;
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
}
