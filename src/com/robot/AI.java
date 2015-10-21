package com.robot;

import com.robot.runtime.info.RuntimeInfo;
import com.robot.spider.Spider;
import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

//    public static void parse_content() {
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
//                    if (string_sentence.length() > 35)
//                        continue;
//                    Sentence sentence = wordSplitor.split_word(string_sentence);
//                    RuntimeInfo.getInstance().update_running_persent((float)(line_index / 672.47));
////                    System.out.println(line_index + " [" + String.format("%.3f", (line_index / 672.47)) + "%] - " + sentence.to_string());
//                }
//                readed_string = reader.readLine();
//                line_index ++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws Exception {
        System.out.println("\n===== Begin =====\n");

//        SentenceSplitor sentenceSplitor = new SentenceSplitor();
//        WordSplitor wordSplitor = new WordSplitor();
        Spider spider = new Spider("Spider");
        spider.start();

        while (true);

//        ArrayList<String> string_sentences = sentenceSplitor.split_sentence(node.toPlainTextString());
//        for (int k = 0; k < string_sentences.size(); k++) {
//            String string_sentence = string_sentences.get(k);
//            if (string_sentence.length() > 35)
//                continue;
//            Sentence sentence = wordSplitor.split_word(string_sentence);
//            RuntimeInfo.getInstance().update_running_persent(0);
//            System.out.println(" - " + sentence.to_string());
//        }

//        System.out.println("\n====== End ======\n");
    }
}
