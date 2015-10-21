package com.robot;

import com.robot.spider.Spider;
import com.robot.split.Splitor;

import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class AI {

    public static void main(String[] args) throws Exception {
        System.out.println("\n===== Begin =====\n");

        ArrayList<Spider> spiders = new ArrayList<Spider>();
        int spiders_count = 500;
        for (int i = 0; i < spiders_count; i ++) {
            Spider spider = new Spider("Spider");
            spiders.add(spider);
            spider.start();
//            Thread.sleep(200);
        }
        Splitor splitor = new Splitor("Splitor");
        splitor.start();

        while (true);

//        System.out.println("\n====== End ======\n");
    }
}
