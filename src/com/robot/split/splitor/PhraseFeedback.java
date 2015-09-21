package com.robot.split.splitor;

import com.robot.database.Connector;
import com.robot.split.model.Sentence;
import com.robot.split.model.Word;
import com.robot.split.model.Phrase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by terry on 15/9/21.
 */
public class PhraseFeedback implements Runnable {

    Thread thread;
    String thread_name;
    boolean is_run;
    Connector db;
    HashMap<String, Phrase> uncommit_phrases;

    public PhraseFeedback(String a_thread_name) {
        thread_name = a_thread_name;
        is_run = false;
        db = new Connector();
        db.connect();
        uncommit_phrases = new HashMap<String, Phrase>();
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
            if (feedback_phrase()) {
                System.out.println(thread_name + " : [ OK uncommit phrases : " + uncommit_phrases.size() + " ]");
            } else {
                System.out.println(thread_name + " : [ Empty uncommit phrases ]");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean feedback_phrase() {
        String key = null;
        Phrase phrase_to_commit = null;
        boolean res = false;
        synchronized (uncommit_phrases) {
            Iterator iter = uncommit_phrases.entrySet().iterator();
            if (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                key = (String)entry.getKey();
                phrase_to_commit = (Phrase)entry.getValue();
            }
            if (key != null && phrase_to_commit != null) {
                uncommit_phrases.remove(key);
                res = true;
            }
        }
        if (res) {
            db.feedback_phrase(phrase_to_commit);
        }
        return res;
    }

    public void feedback_sentence(Sentence a_sentence) {
        for (int i = 0; i < a_sentence.length() - 1; i ++) {
            Phrase phrase = new Phrase(a_sentence.at(i), a_sentence.at(i + 1));
            add_phrase_to_feedback_list(phrase);
        }
    }

    public void add_phrase_to_feedback_list(Phrase a_phrase)
    {
        String key = a_phrase.to_string();
        if (key == null) return;
        Phrase phrase = null;
        synchronized (uncommit_phrases) {
            if (uncommit_phrases.containsKey(key)) {
                phrase = uncommit_phrases.get(key);
            }
            if (phrase == null) {
                a_phrase.uncommit_feedback_times = 1;
                uncommit_phrases.put(key, a_phrase);
            } else {
                phrase.uncommit_feedback_times++;
                uncommit_phrases.put(key, phrase);
            }
        }
    }
}
