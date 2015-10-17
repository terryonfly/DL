package com.robot.runtime.info;

/**
 * Created by terry on 15/10/17.
 */
//饿汉式单例类.在类初始化时，已经自行实例化
public class RuntimeInfo {

    private float running_persent = 0;

    private int uncommit_word_count = 0;

    private int uncommit_phrase_count = 0;

    private RuntimeInfo() {}

    private static final RuntimeInfo runtimeInfo = new RuntimeInfo();

    //静态工厂方法
    public static RuntimeInfo getInstance() {
        return runtimeInfo;
    }

    public void print_runtime_info() {
        System.out.print("running[" + String.format("%.3f", running_persent) + "%] word[" + String.format("%6d", uncommit_word_count) + "] phrase[" + String.format("%6d", uncommit_phrase_count) + "]\r");
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
}
