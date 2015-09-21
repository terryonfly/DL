package com.robot.split.cache;

import com.robot.database.Connector;
import com.robot.split.model.Word;
import com.robot.split.model.Phrase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by terry on 15/9/22.
 */
public class CorpusPhrase {

    Connector db;
    String table;
    HashMap<String, Phrase> cache_phrase;

    public CorpusPhrase(String a_table) {
        db = new Connector();
        table = a_table;
        cache_phrase = new HashMap<String, Phrase>();
        pre_loading();
    }

    public void pre_loading() {
        System.out.println("Starting '" + table + "' pre loading...");
        int page_i = 0;
        int page_size = 100000;
        boolean hasnext = true;
        do {
            System.out.println("Loading " + page_i + " ...");
            ArrayList<Phrase> phrases = db.pre_load_phrases(table, page_i * page_size, page_size);
            if (phrases.size() == 0)
                hasnext = false;
            for (int i = 0; i < phrases.size(); i ++) {
                add_to_cache(phrases.get(i).to_string(), phrases.get(i));
            }
            page_i ++;
        } while (hasnext);
        System.out.println("Pre loading for '" + table + "' did finished : " + cache_phrase.size());
    }

    public void add_to_cache(String a_string_word, Phrase a_phrase) {
        String key = a_string_word;
        Phrase value = a_phrase;
        if (key == null || value == null) return;
        cache_phrase.put(key, value);
    }

    public Phrase search_from_cache(String a_string_phrase) {
        String key = a_string_phrase;
        if (key == null) return null;
        Phrase phrase = null;
        if (cache_phrase.containsKey(key)) {
            phrase = cache_phrase.get(key);
        }
        return phrase;
    }
}
