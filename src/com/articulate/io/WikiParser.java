package com.articulate.io;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by devilo on 24/3/14.
 */
public class WikiParser {

    public static final String WORD_MAP_ID_FIELD = "_id";
    public static final String WORD_MAP_DATA_FIELD = "_data";
    public static final Character DELIM = ' ';

    /**
     * For a given line in title file; this method returns the map that has 2 entries
     * 1. _id -> contains the id of the page
     * 2. _data -> contains the treeset of the words that are identified in the title of the wiki page
     * @param line a line in the wiki title file
     * @return map
     */
    public static HashMap<String, TreeSet<String>> getWordDocIdMap(String line) {

        HashMap<String , TreeSet<String>> wordMap = new HashMap<String, TreeSet<String>>();
        wordMap.put(WORD_MAP_ID_FIELD , new TreeSet<String>());
        wordMap.put(WORD_MAP_DATA_FIELD , new TreeSet<String>());

        int i = 0;
        int length = line.length();
        StringBuilder word = new StringBuilder();

        for ( i = 0 ; i < length ; i++ ) {
            char currentChar = line.charAt(i);

            if ( currentChar == DELIM ) {
                break;
            }
            word.append(currentChar);
        }

        wordMap.get(WORD_MAP_ID_FIELD).add(new String(word));
        TreeSet<String> listOfWords = wordMap.get(WORD_MAP_DATA_FIELD);

        word.setLength(0);
        for ( i++ ; i < length ; i++ ) {
            char currentChar = line.charAt(i);

            if ( Character.isWhitespace(currentChar) ) {
                listOfWords.add(new String(word));
                word.setLength(0);
            }
            else {
                word.append(currentChar);
            }
        }

        if ( word.length() > 0 )
            listOfWords.add(new String(word));

        return wordMap;
    }
}
