package com.robot.spider;

import com.robot.database.Connector;
import com.robot.runtime.info.RuntimeInfo;
import com.robot.split.model.Sentence;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by terry on 15/10/21.
 */
public class Spider implements Runnable {

    Thread thread;
    String thread_name;
    boolean is_run;
    Connector db;
    URLQueue urlQueue;

    public Spider(String a_thread_name) {
        thread_name = a_thread_name;
        is_run = false;
        db = new Connector();
        db.connect();
        urlQueue = new URLQueue("URL Queue");
        urlQueue.start();
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
            PageAnalyzer pageAnalyzer = new PageAnalyzer();
            while (is_run) {
                String target_url = urlQueue.get_one_url();
                if (target_url.length() > 0) {
                    System.out.printf("%s\n", target_url);
                    pageAnalyzer.set_taget_url(target_url);
                    // Content Data
                    ArrayList<String> content_datas = pageAnalyzer.getContentDatas();
                    for (int i = 0; i < content_datas.size(); i ++) {
                            ArrayList<String> string_sentences = sentenceSplitor.split_sentence(content_datas.get(i));
                            for (int k = 0; k < string_sentences.size(); k++) {
                                String string_sentence = string_sentences.get(k);
                                if (string_sentence.length() > 35 || string_sentence.length() == 0)
                                    continue;
                                if (!wordSplitor.isMessyCode(string_sentence)) {
                                    Sentence sentence = wordSplitor.split_word(string_sentence);
//                                    RuntimeInfo.getInstance().update_running_sentence(sentence.to_string());
                                    System.out.printf("%s\n", sentence.to_string());
                                }
                            }
                    }
                    // Links
                    urlQueue.add_urls(pageAnalyzer.getLinks());
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
