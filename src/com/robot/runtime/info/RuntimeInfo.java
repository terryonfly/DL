package com.robot.runtime.info;

/**
 * Created by terry on 15/10/17.
 */
//饿汉式单例类.在类初始化时，已经自行实例化
public class RuntimeInfo {

    private float running_persent = 0;

    private int uncommit_word_count = 0;

    private int uncommit_phrase_count = 0;

    private int uncommit_url_count = 0;

    private String running_sentence = "";

    private RuntimeInfo() {}

    private static final RuntimeInfo runtimeInfo = new RuntimeInfo();

    //静态工厂方法
    public static RuntimeInfo getInstance() {
        return runtimeInfo;
    }

    public void print_runtime_info() {
        System.out.print("running[" + String.format("%.3f", running_persent) + "%] word[" + String.format("%7d", uncommit_word_count) + "] phrase[" + String.format("%7d", uncommit_phrase_count) + "] url[" + String.format("%5d", uncommit_url_count) + "] " + String.format("%40s", running_sentence) + "\r");
    }

    public void update_running_persent(float a_running_persent) {
        running_persent = a_running_persent;
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
        uncommit_url_count = a_uncommit_url_count;
        print_runtime_info();
    }

    public void update_running_sentence(String a_running_sentence) {
        running_sentence = a_running_sentence;
        print_runtime_info();
    }
}
