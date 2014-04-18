package com.articulate.util;

import com.articulate.io.DictionaryIO;
import com.articulate.io.IOUtil;

import java.io.*;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by devilo on 21/3/14.
 */
public class ArticulateNERBackup {

    static final String NOUN_DICTIONARY_PATH = "files/noun.dict";
    static final String OTHER_DICTIONARY_PATH = "files/other.dict";

    private HashedTrie nounDictionaryTrie = new HashedTrie();
    private HashedTrie otherDictionaryTrie = new HashedTrie();

    public void init() {
        DictionaryIO dictionaryIO = new DictionaryIO(nounDictionaryTrie , otherDictionaryTrie);
        dictionaryIO.createTrie(NOUN_DICTIONARY_PATH , OTHER_DICTIONARY_PATH);
    }

    public void processSingleArticle() {

        TreeMap<String , Integer> termFrequency = new TreeMap<String , Integer>();
        /**
         * Read articles and print entities in it
         */

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("files/input.txt"));

            String line = null;
            
            boolean print_entity = false;
            
            while ( (line=bufferedReader.readLine()) != null ) {

            	//TODO: Remove the links present in the text
                line = line.replace("." , "");
                
                StringBuilder namedEntity = new StringBuilder();
                
                String array[] = line.split("[^0-9a-zA-Z-']");
                for ( String token : array ) {
                    token = token.trim().toLowerCase();
                    if (token.length() > 0 && !Classifiers.isStopword(token)) {
                        
                    	if ( nounDictionaryTrie.contains(token) || (!nounDictionaryTrie.contains(token) && !otherDictionaryTrie.contains(token))) {
                            namedEntity.append(token);
                            namedEntity.append(' ');
                        }
                        else {
                        	print_entity = true;
                        }
                    }
                    else {
                    	print_entity = true;
                    }
                    
                    if ( print_entity ) {
                    	if ( namedEntity.length() > 0 ) {

                            String namedEntityStr = new String ( namedEntity );
                            Integer frequency = termFrequency.get(namedEntityStr);
                            if ( frequency == null ) {
                                termFrequency.put(namedEntityStr , 0);
                            }

                            termFrequency.put(namedEntityStr , termFrequency.get(namedEntityStr) + 1);

                        }
                    	namedEntity.setLength(0);
                    	print_entity = false;
                    }

                }
            }

            bufferedReader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("files/candidate_ner"));
            for ( String term : termFrequency.keySet() ) {
                writer.write( term + "< " + termFrequency.get(term) + "\n");
            }
            IOUtil.closeClosable(writer);

            System.out.println("files/candidate_ner created.");

        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void processArticles(List<Article> listOfArticles) {

    }
}
