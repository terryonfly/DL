package com.robot;

import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

    public static void main(String[] args) {
        System.out.println("\n===== Begin =====\n");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String readed_string = reader.readLine();
            SentenceSplitor sentenceSplitor = new SentenceSplitor();
            WordSplitor wordSplitor = new WordSplitor();
            int line_index = 0;
            while (readed_string != null) {
                ArrayList<String> string_sentences = sentenceSplitor.split_sentence(readed_string);
                for (int i = 0; i < string_sentences.size(); i++) {
                    String string_sentence = string_sentences.get(i);
                    if (string_sentence.length() > 35)
                        continue;
                    Sentence sentence = wordSplitor.split_word(string_sentence);
                    System.out.println(line_index + " [" + String.format("%.3f", (line_index / 672.47)) + "%] - " + sentence.to_string());
                }
                readed_string = reader.readLine();
                line_index ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n====== End ======\n");
    }
}
