package com.articulate.util;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import com.articulate.io.DictionaryIO;
import com.articulate.io.IOUtil;

/**
 * Created by devilo on 21/3/14.
 */
public class ArticulateNER {

    static final String NOUN_DICTIONARY_PATH = "files/noun.dict";
    static final String OTHER_DICTIONARY_PATH = "files/other.dict";

    private HashedTrie nounDictionaryTrie = new HashedTrie();
    private HashedTrie otherDictionaryTrie = new HashedTrie();

    public void init() {
        DictionaryIO dictionaryIO = new DictionaryIO(nounDictionaryTrie, otherDictionaryTrie);
        dictionaryIO.createTrie(NOUN_DICTIONARY_PATH, OTHER_DICTIONARY_PATH);
    }

    public LinkedHashMap<String , LinkedHashMap<String, Integer>> processSingleArticle(Article article, int index) {

        TreeMap<String, Integer> termFrequency = new TreeMap<String, Integer>();
        /**
         * Read articles and print entities in it
         */

        String line = article.getBody();

        boolean print_entity = false;

        line = line.replace(".", "");

        StringBuilder namedEntity = new StringBuilder();

        String array[] = line.split("[^0-9a-zA-Z-']");
        for (String token : array) {
            token = token.trim().toLowerCase();
            if (token.length() > 0 && !Classifiers.isStopword(token)) {

                if (nounDictionaryTrie.contains(token) || (!nounDictionaryTrie.contains(token) && !otherDictionaryTrie.contains(token))) {
                    namedEntity.append(token);
                    namedEntity.append(' ');
                } else {
                    print_entity = true;
                }
            } else {
                print_entity = true;
            }

            if (print_entity) {
                if (namedEntity.length() > 0) {

                    String namedEntityStr = new String(namedEntity);
                    Integer frequency = termFrequency.get(namedEntityStr);
                    if (frequency == null) {
                        termFrequency.put(namedEntityStr, 0);
                    }

                    termFrequency.put(namedEntityStr, termFrequency.get(namedEntityStr) + 1);

                }
                namedEntity.setLength(0);
                print_entity = false;
            }

        }

        try {

            File f = new File("files/candidate_ner" + index);
            if(f.exists() && !f.isDirectory()) {
                f.delete();
            }

            f = new File("files/articles/output_ner" + index);
            if(f.exists() && !f.isDirectory()) {
                f.delete();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("files/candidate_ner" + index));
            for ( String term : termFrequency.keySet() ) {
                writer.write( term + "< " + termFrequency.get(term) + "\n");
            }
            IOUtil.closeClosable(writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("scripts/ner.sh files/candidate_ner"+ index +"files/articles/output_ner" + index);
            Process execProcess = Runtime.getRuntime().exec("scripts/ner.sh files/candidate_ner" + index + " files/articles/output_ner" + index);
            execProcess.waitFor();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        NerOutputParser outputParser = new NerOutputParser("files/articles/output_ner" + index);
        return outputParser.getEntityRepr();

    }

    public LinkedHashMap<String , LinkedHashMap<String , LinkedHashMap<String, Integer>>> processArticles(List<Article> listOfArticles) {

        LinkedHashMap<String , LinkedHashMap<String , LinkedHashMap<String, Integer>>> map = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Integer>>>();

        int index = 1;
        for ( Article article : listOfArticles ) {
            LinkedHashMap<String , LinkedHashMap<String, Integer>> mapi = processSingleArticle(article , index);
            map.put(article.getTitle() , mapi);
            System.out.println("Putting for value : " + article.getTitle());
            index++;
        }


        try {
            Process execProcess = Runtime.getRuntime().exec("scripts/sentiment_analysis/run.sh");
            execProcess.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        LinkedHashMap<String , LinkedHashMap<String, Integer>> results = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("files/out.txt"));

            String line;

            while ( (line = bufferedReader.readLine()) != null ) {
                results.put(line , new LinkedHashMap<String, Integer>());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("OUTPUT" , results);

        return map;
    }
}
