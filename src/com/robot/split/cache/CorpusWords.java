package com.robot.split.cache;

import com.robot.database.Connector;
import com.robot.split.model.Word;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by terry on 15/9/20.
 */
public class CorpusWords {

    Connector db;
    String table;
    HashMap<String, Word> cache_words;

    public CorpusWords(String a_table) {
        db = new Connector();
        table = a_table;
        cache_words = new HashMap<String, Word>();
        pre_loading();
        db.disconnect();
    }

    public void pre_loading() {
        System.out.println("Starting '" + table + "' pre loading...");
        int page_i = 0;
        int page_size = 10000;
        boolean hasnext = true;
        do {
            ArrayList<Word> words = db.pre_load_words(table, page_i * page_size, page_size);
            if (words.size() == 0)
                hasnext = false;
            for (int i = 0; i < words.size(); i ++) {
                add_to_cache(words.get(i).to_string(), words.get(i));
            }
            page_i ++;
        } while (hasnext);
        System.out.println("Pre loading for '" + table + "' did finished : " + cache_words.size());
    }

    public void add_to_cache(String a_string_word, Word a_word) {
        String key = a_string_word;
        Word value = a_word;
        if (key == null || value == null) return;
        cache_words.put(key, value);
    }

    public Word search_from_cache(String a_string_word) {
        String key = a_string_word;
        if (key == null) return null;
        Word word = null;
        if (cache_words.containsKey(key)) {
            word = cache_words.get(key);
        }
        return word;
    }
}
