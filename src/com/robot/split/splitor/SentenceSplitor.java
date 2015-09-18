package com.robot.split.splitor;

import java.util.ArrayList;

/**
 * Created by terry on 15/9/18.
 */
public class SentenceSplitor {

    String end_en_symbol[] = {" ",",",":",";","!","?","\r","\n"};
    String normal_en_symbol[] = {" ","!","\\","*","(",")","-","_","+","=","{","}","[","]",":",";","\"","\"",",","<",">","?","/","|","@","#","$","%","^","&"};

    String end_zh_symbol[] = {"。","，","：","；","！","？"};
    String normal_zh_symbol[] = {"　","。","，","：","；","！","？","～","@","#","￥","%","……","（","）","——","｛","｝","【","】","“","”","‘","’","《","》","、","`"};


    public SentenceSplitor() {

    }

    public CharType check_char_type(String a_string_char) {
        if (a_string_char.getBytes().length == a_string_char.length()) {// en
            for (int i = 0; i < end_en_symbol.length; i ++) {
                if (a_string_char.equals(end_en_symbol[i])) {
                    return CharType.CharTypeEnEndSymbol;
                }
            }
            for (int i = 0; i < normal_en_symbol.length; i ++) {
                if (a_string_char.equals(normal_en_symbol[i])) {
                    return CharType.CharTypeEnSymbol;
                }
            }
            return CharType.CharTypeEn;
        } else {// zh
            for (int i = 0; i < end_zh_symbol.length; i ++) {
                if (a_string_char.equals(end_zh_symbol[i])) {
                    return CharType.CharTypeZhEndSymbol;
                }
            }
            for (int i = 0; i < normal_zh_symbol.length; i ++) {
                if (a_string_char.equals(normal_zh_symbol[i])) {
                    return CharType.CharTypeZhSymbol;
                }
            }
            return CharType.CharTypeZh;
        }
    }

    public ArrayList<String> split_sentence(String a_string) {
        ArrayList<String> string_sentences = new ArrayList<String>();
        String string_sentence = "";
        for (int i = 0; i < a_string.length(); i ++) {
            switch (check_char_type(a_string.substring(i, i + 1))) {
                case CharTypeZh:
                {
                    string_sentence += a_string.substring(i, i + 1);
                }
                break;
                case CharTypeEn:
                {
//                    string_sentence += a_string.substring(i, i + 1);
                }
                break;
                case CharTypeEnSymbol:
                {
                    // Do nothing
                }
                break;
                case CharTypeZhSymbol:
                {
                    // Do nothing
                }
                break;
                case CharTypeEnEndSymbol:
                {
                    if (string_sentence.length() > 0) {
                        string_sentences.add(string_sentence);
                        string_sentence = "";
                    }
                }
                break;
                case CharTypeZhEndSymbol:
                {
                    if (string_sentence.length() > 0) {
                        string_sentences.add(string_sentence);
                        string_sentence = "";
                    }
                }
                break;
            }
        }
        if (string_sentence.length() > 0) {
            string_sentences.add(string_sentence);
        }
        return string_sentences;
    }
}
