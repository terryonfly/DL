package com.robot;

import com.robot.database.Connector;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

    public static void main(String[] args) {
        System.out.println("===== Begin =====");
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
//            String readed_string = reader.readLine();
//            SentenceSplitor sentenceSplitor = new SentenceSplitor();
//            WordSplitor wordSplitor = new WordSplitor();
//            int line_index = 0;
//            while (readed_string != null) {
//                ArrayList<String> string_sentences = sentenceSplitor.split_sentence(readed_string);
//                for (int i = 0; i < string_sentences.size(); i++) {
//                    String string_sentence = string_sentences.get(i);
////                    System.out.println("=================");
////                    System.out.println("Checking : " + string_sentence);
//                    if (string_sentence.length() > 40)
//                        continue;
//                    Sentence sentence = wordSplitor.split_word(string_sentence);
//                    System.out.println(line_index + " [" + String.format("%.3f", (line_index / 672.48)) + "%] - " + sentence.to_string());
////                    System.out.println();
//                }
//                readed_string = reader.readLine();
//                line_index ++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        int page_i = 0;
        int page_size = 3000;
        Connector db = new Connector();
        ArrayList<Word> words_sogou = new ArrayList<Word>();
        HashMap<String, Word> words_cn = new HashMap<String, Word>();
        boolean hasnext = true;
        do {
            System.out.println("load sogou " + (page_i * page_size) + " page.");
            ArrayList<Word> words = db.preload_words("word_sogou", page_i * page_size, page_size);
            for (int i = 0; i < words.size(); i ++) {
                words_sogou.add(words.get(i));
            }
            if (words.size() == 0)
                hasnext = false;
            page_i ++;
        } while (hasnext);

        page_i = 0;
        hasnext = true;
        do {
            System.out.println("load cn " + (page_i * page_size) + " page.");
            ArrayList<Word> words = db.preload_words("word_cn", page_i * page_size, page_size);
            for (int i = 0; i < words.size(); i ++) {
                words_cn.put(words.get(i).to_string(), words.get(i));
            }
            if (words.size() == 0)
                hasnext = false;
            page_i ++;
        } while (hasnext);

        for (int i = 0; i < words_sogou.size(); i ++) {
            System.out.println("Check " + i);
            if (words_sogou.get(i).get_word_type().equals("|")) {// if word_sogou has word and has no type
                Word pre_first_word = words_cn.get(words_sogou.get(i).to_string());
                if (pre_first_word != null) {
                    if (!pre_first_word.get_word_type().equals("|") && !pre_first_word.get_word_type().equals("||")) {
                        System.out.println("Fixed word (" + pre_first_word.to_string() + ")");
                        db.update_word(pre_first_word, "word_sogou");
                    }
                }
            }
        }
        System.out.println("====== End ======");
    }
}
