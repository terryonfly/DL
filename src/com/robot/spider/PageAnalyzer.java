package com.robot.spider;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by terry on 15/10/20.
 */
public class PageAnalyzer {

    String html;
    Parser parser;
    WebLoader webLoader;

    public PageAnalyzer(WebLoader a_webLoader) {
        parser = new Parser();
        webLoader = a_webLoader;
    }

    public void setHTML(String a_html) {
        html = a_html;
    }

    public ArrayList<String> getLinks() {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            parser.setResource(html);
            NodeClassFilter linkFilter = new NodeClassFilter(LinkTag.class);
            NodeList linkList = parser.parse(linkFilter);
            for (int i = 0; i < linkList.size(); i ++) {
                LinkTag node = (LinkTag)linkList.elementAt(i);
                String url = node.getLink();
                String contain = " '\"";
                if (url.startsWith("http") && !url.contains(contain) && checkUrl(url)) {
                    urls.add(url);
                }
            }
        } catch (ParserException e) {
            System.out.printf("getLinks err : %s\n", e);
            e.printStackTrace();
            return urls;
        }
        return urls;
    }

    public static boolean checkUrl(String url) {
        return url.matches("^((https|http|ftp|rtsp|mms)?://)"
                + "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,4})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
    }

    public ArrayList<String> getContentDatas() {
        ArrayList<String> content_datas = new ArrayList<String>();
        try {
            parser.setResource(html);
            NodeList contentList = parser.parse(null);
            for (int i = 0; i < contentList.size(); i ++) {
                Node node = contentList.elementAt(i);
                String content_data = node.toPlainTextString();
                if (content_data != null && content_data.length() > 0) {
                    content_datas.add(content_data);
                }
            }
        } catch (ParserException e) {
            System.out.printf("getContentDatas err : %s\n", e);
            e.printStackTrace();
            return content_datas;
        }
        return content_datas;
    }
}
