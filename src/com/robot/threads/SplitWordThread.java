package com.robot.threads;

import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by terry on 15/9/20.
 */
public class SplitWordThread implements Runnable {

    int thread_id;

    boolean is_free;
    boolean need_run;
    String need_string_sentence;

    public SplitWordThread(int a_thread_id) {
        thread_id = a_thread_id;
    }

    @Override
    public void run() {
        is_free = true;
        need_run = false;
        WordSplitor wordSplitor = new WordSplitor();
        while (true) {
            if (need_run) {
                need_run = false;
                String string_sentence = need_string_sentence;
//                System.out.println("=================");
//                System.out.println("Checking : " + string_sentence);
                if (string_sentence.length() > 40) {
                    is_free = true;
                    continue;
                }
                Sentence sentence = wordSplitor.split_word(string_sentence);
                System.out.println("Thread " + thread_id + " ---- " + sentence.to_string());
//                System.out.println();
                is_free = true;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean push_new_sentence(String a_sentence) {
        if (!is_free) return false;
        need_string_sentence = a_sentence;
        is_free = false;
        need_run = true;
        return true;
    }
}
