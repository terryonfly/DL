package com.robot.spider;

import com.robot.database.Connector;
import com.robot.runtime.info.RuntimeInfo;
import com.robot.split.model.Phrase;

import java.util.*;

/**
 * Created by terry on 15/10/20.
 */
public class URLQueue implements Runnable {

    Thread thread;
    String thread_name;
    boolean is_run;
    Connector db;
    Connector db_get_url;
    ArrayList<String> uncommit_urls;
    String seed_url = "http://www.baidu.com";

    public URLQueue(String a_thread_name) {
        thread_name = a_thread_name;
        is_run = false;
        db = new Connector();
        db.connect();
        db_get_url = new Connector();
        db_get_url.connect();
        uncommit_urls = new ArrayList<String>();
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
            if (commit_url()) {
                RuntimeInfo.getInstance().update_uncommit_url_count(uncommit_urls.size());
                //System.out.println(thread_name + " : [ OK uncommit url : " + uncommit_urls.size() + " ]");
            } else {
                RuntimeInfo.getInstance().update_uncommit_url_count(uncommit_urls.size());
                //System.out.println(thread_name + " : [ Empty uncommit url ]");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean commit_url() {
        String url_to_commit = null;
        boolean res = false;
        synchronized (uncommit_urls) {
            if (uncommit_urls.size() > 0) {
                url_to_commit = uncommit_urls.get(0);
                uncommit_urls.remove(0);
                res = true;
            }
        }
        if (res) {
            db.add_url(url_to_commit);
        }
        return res;
    }

    public void add_urls(ArrayList<String> a_urls) {
        if (a_urls.size() == 0) return;
        synchronized (uncommit_urls) {
            for (int i = 0; i < a_urls.size(); i ++) {
                uncommit_urls.add(a_urls.get(i));
            }
        }
    }

    public String get_one_url() {
        String url = "";
        synchronized (db_get_url) {
            url = db_get_url.get_url();
        }
        if (url.length() == 0) {
            url = seed_url;
            seed_url = "";
        }
        return url;
    }
}
