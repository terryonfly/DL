package com.robot;

import com.robot.runtime.info.RuntimeInfo;
import com.robot.spider.Spider;
import com.robot.spider.URLCache;
import com.robot.spider.URLQueue;
import com.robot.split.Splitor;

import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

    public static void main(String[] args) throws Exception {
        System.out.println("\n===== Begin =====\n");

        boolean only_load = false;
        if (args.length > 0) {
            if (args[0].equals("load")) {
                only_load = true;
            }
        }

        URLQueue urlQueue = new URLQueue("URL Queue");
        urlQueue.start();
        ArrayList<Spider> spiders = new ArrayList<Spider>();
        int spiders_count = 200;
        for (int i = 0; i < spiders_count; i ++) {
            System.out.printf("run %dth 'Spider' thread\n", i);
            Spider spider = new Spider("Spider", urlQueue);
            spiders.add(spider);
            spider.start();
//            Thread.sleep(3000);
        }
        if (!only_load) {
            Splitor splitor = new Splitor("Splitor");
            splitor.start();
        }

        while (true) {
            RuntimeInfo.getInstance().print_runtime_info();
            Thread.sleep(1000);
        }

//        System.out.println("\n====== End ======\n");
    }
}
