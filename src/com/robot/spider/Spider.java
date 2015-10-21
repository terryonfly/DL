package com.robot.spider;

import com.robot.database.Connector;
import com.robot.runtime.info.RuntimeInfo;
import com.robot.split.cache.CorpusWords;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;
import com.robot.split.splitor.SentenceSplitor;
import com.robot.split.splitor.WordSplitor;

import java.util.ArrayList;

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
        CorpusWords cache_chars_unnormal = new CorpusWords("char_unnormal");
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
                            if (!isMessyCode(cache_chars_unnormal, string_sentence)) {
                                db.add_web_content(string_sentence);
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

    public boolean isMessyCode(CorpusWords cache_chars_unnormal, String a_string_sentence) {
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

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }
}
