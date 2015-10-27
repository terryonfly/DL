package com.robot.runtime.info;

/**
 * Created by terry on 15/10/17.
 */
//饿汉式单例类.在类初始化时，已经自行实例化
public class RuntimeInfo {

    private UncommitCount running_page_count = new UncommitCount();

    private int uncommit_word_count = 0;

    private int uncommit_phrase_count = 0;

    private UncommitCount uncommit_url_count = new UncommitCount();

    private String running_sentence = "";

    private RuntimeInfo() {}

    private static final RuntimeInfo runtimeInfo = new RuntimeInfo();

    //静态工厂方法
    public static RuntimeInfo getInstance() {
        return runtimeInfo;
    }

    public void print_runtime_info() {
        System.out.print("pages[" + String.format("%6d", running_page_count.count) + "] word[" + String.format("%7d", uncommit_word_count) + "] phrase[" + String.format("%7d", uncommit_phrase_count) + "] url[" + String.format("%7d", uncommit_url_count.count) + "] " + String.format("%s", running_sentence) + "\r");
    }

    public void update_running_page_count(int a_running_page_count) {
        synchronized (running_page_count) {
            running_page_count.count += a_running_page_count;
        }
        print_runtime_info();
    }

    public void update_uncommit_word_count(int a_uncommit_word_count) {
        uncommit_word_count = a_uncommit_word_count;
        print_runtime_info();
    }

    public void update_uncommit_phrase_count(int a_uncommit_phrase_count) {
        uncommit_phrase_count = a_uncommit_phrase_count;
        print_runtime_info();
    }

    public void update_uncommit_url_count(int a_uncommit_url_count) {
	    synchronized (uncommit_url_count) {
            uncommit_url_count.count += a_uncommit_url_count;
	    }
        print_runtime_info();
    }

    public void update_running_sentence(String a_running_sentence) {
        running_sentence = a_running_sentence;
        print_runtime_info();
    }
}

