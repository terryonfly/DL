package com.robot;

import java.io.*;
import java.util.ArrayList;

import com.robot.split.model.Sentence;
import com.robot.split.splitor.*;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

    public static void main(String[] args) {
        System.out.println("===== Begin =====");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String readed_string = reader.readLine();
            SentenceSplitor sentenceSplitor = new SentenceSplitor();
            WordSplitor wordSplitor = new WordSplitor();
            int line_index = 0;
            while (readed_string != null) {
                System.out.println("Current line num : " + line_index);
                ArrayList<String> string_sentences = sentenceSplitor.split_sentence(readed_string);
                for (int i = 0; i < string_sentences.size(); i++) {
                    String string_sentence = string_sentences.get(i);
                    System.out.println("=================");
                    System.out.println("Checking : " + string_sentence);
                    if (string_sentence.length() > 40)
                        continue;
                    Sentence sentence = wordSplitor.split_word(string_sentence);
                    System.out.println("Result : " + sentence.to_string());
                    System.out.println();
                }
                readed_string = reader.readLine();
                line_index ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("====== End ======");
    }
}
