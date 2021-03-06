package com.robot.split.splitor;

import com.robot.database.Connector;
import com.robot.runtime.info.RuntimeInfo;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Created by terry on 15/9/19.
 */
public class WordFeedback implements Runnable {

    Thread thread;
    String thread_name;
    public boolean is_run;
    Connector db;
    HashMap<String, Word> uncommit_words;

    public WordFeedback(String a_thread_name) {
        thread_name = a_thread_name;
        is_run = false;
        db = new Connector();
        db.connect();
        uncommit_words = new HashMap<String, Word>();
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
        while (is_run) {
            if (feedback_word()) {
                RuntimeInfo.getInstance().update_uncommit_word_count(uncommit_words.size());
//                System.out.print(thread_name + " : [ OK uncommit words : " + uncommit_words.size() + " ]\r");
            } else {
                RuntimeInfo.getInstance().update_uncommit_word_count(uncommit_words.size());
//                System.out.print(thread_name + " : [ Empty uncommit words ]\r");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean feedback_word() {
        String key = null;
        Word word_to_commit = null;
        boolean res = false;
        synchronized (uncommit_words) {
            Iterator iter = uncommit_words.entrySet().iterator();
            if (iter.hasNext()) {
                Entry entry = (Entry)iter.next();
                key = (String)entry.getKey();
                word_to_commit = (Word)entry.getValue();
            }
            if (key != null && word_to_commit != null) {
                uncommit_words.remove(key);
                res = true;
            }
        }
        if (res) {
            db.feedback_word(word_to_commit);
        }
        return res;
    }

    public void feedback_sentence(Sentence a_sentence) {
        for (int i = 0; i < a_sentence.length(); i ++) {
            add_word_to_feedback_list(a_sentence.at(i));
        }
    }

    public void add_word_to_feedback_list(Word a_word)
    {
        String key = a_word.to_string();
        if (key == null) return;
        Word word = null;
        synchronized (uncommit_words) {
            if (uncommit_words.containsKey(key)) {
                word = uncommit_words.get(key);
            }
            if (word == null) {
                a_word.uncommit_feedback_times = 1;
                uncommit_words.put(key, a_word);
            } else {
                word.uncommit_feedback_times++;
                uncommit_words.put(key, word);
            }
        }
    }
}
