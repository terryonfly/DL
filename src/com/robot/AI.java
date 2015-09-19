package com.robot;

import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;
import com.robot.threads.*;

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
        System.out.println("===== Begin =====");
        int thread_count = 9;
        ArrayList<SplitWordThread> splitWordThreads = new ArrayList<SplitWordThread>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < thread_count; i ++) {
            SplitWordThread splitWordThread = new SplitWordThread(i + 1);
            splitWordThreads.add(splitWordThread);
            Thread thread = new Thread(splitWordThread);
            threads.add(thread);
            thread.start();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String readed_string = reader.readLine();
            SentenceSplitor sentenceSplitor = new SentenceSplitor();
            int line_index = 0;
            while (readed_string != null) {
//                System.out.println("Current line num : " + line_index);
                ArrayList<String> string_sentences = sentenceSplitor.split_sentence(readed_string);
                for (int i = 0; i < string_sentences.size(); i++) {
                    String string_sentence = string_sentences.get(i);
                    boolean has_push = false;
                    while (!has_push) {
                        Thread.sleep(10);
                        for (int th_index = 0; th_index < thread_count; th_index ++) {
                            has_push = splitWordThreads.get(th_index).push_new_sentence(string_sentence);
                            if (has_push)
                                break;
                        }
                    }
//                    System.out.println("=================");
//                    System.out.println("Checking : " + string_sentence);
//                    if (string_sentence.length() > 40)
//                        continue;
//                    Sentence sentence = wordSplitor.split_word(string_sentence);
//                    System.out.println("Result : " + sentence.to_string());
//                    System.out.println();
                }
                readed_string = reader.readLine();
                line_index ++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("====== End ======");
    }
}
