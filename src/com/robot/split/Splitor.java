package com.robot.split;

import com.robot.database.Connector;
import com.robot.spider.URLQueue;
import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.util.ArrayList;

/**
 * Created by terry on 15/10/22.
 */
public class Splitor implements Runnable {

    Thread thread;
    String thread_name;
    boolean is_run;
    Connector db;

    public Splitor(String a_thread_name) {
        thread_name = a_thread_name;
        is_run = false;
        db = new Connector();
        db.connect();
    }

    public void start() {
        if (is_run) return;
        thread = new Thread(this);
        is_run = true;
        thread.start();
    }

    public void stop() {
        if (!is_run) return;
        is_run = false;
        thread = null;
    }

    @Override
    public void run() {
        SentenceSplitor sentenceSplitor = new SentenceSplitor();
        WordSplitor wordSplitor = new WordSplitor();
        try {
            while (is_run) {
                String content_data = db.get_web_content();
                if (content_data.length() > 0) {
                    ArrayList<String> string_sentences = sentenceSplitor.split_sentence(content_data);
                    for (int k = 0; k < string_sentences.size(); k++) {
                        String string_sentence = string_sentences.get(k);
                        if (string_sentence.length() > 35 || string_sentence.length() == 0)
                            continue;
                        if (!wordSplitor.isMessyCode(string_sentence)) {
                            Sentence sentence = wordSplitor.split_word(string_sentence);
//                            RuntimeInfo.getInstance().update_running_sentence(sentence.to_string());
//                            System.out.printf("%s\n", sentence.to_string());
                        }
                    }
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }
}
