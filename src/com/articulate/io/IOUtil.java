package com.articulate.io;

import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by devilo on 21/3/14.
 */
public class IOUtil {

    /**
     * The file is of the structure
     * doc_id1:title1
     * doc_id2:title2
     * doc_id3:title3
     *
     * @param titleFilePath the path of the title file.
     */
    public static void createTitleIndex( String titleFilePath , String indexFilePath ) {

        System.out.println("Creating title index ...");
        System.out.println("TITLE FILE PATH : " + titleFilePath);
        System.out.println("INDEX FILE PATH : " + indexFilePath);

        /*
         *  This map will store the mapping between
         *      word -> doc ids
         *      doc_ids is the list of doc_ids where the word is present.
         */
        TreeMap<String , TreeSet<String>> invertedIndex = new TreeMap<String, TreeSet<String>>();

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(titleFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;

        try {

            while ( (line = bufferedReader.readLine()) != null ) {

                HashMap<String , TreeSet<String>> wordToDocIdsMap = WikiParser.getWordDocIdMap(line);
                String _id = wordToDocIdsMap.get(WikiParser.WORD_MAP_ID_FIELD).first();
                TreeSet<String> _data = wordToDocIdsMap.get(WikiParser.WORD_MAP_DATA_FIELD);

                for ( String word: _data ) {

                    TreeSet<String> listDocIds = invertedIndex.get(word);

                    if ( listDocIds == null ) {
                        listDocIds = new TreeSet<String>();
                        listDocIds.add(_id);
                        invertedIndex.put(word , listDocIds);
                    }
                    else {
                        listDocIds.add(_id);
                    }
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        closeClosable(bufferedReader);

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(indexFilePath));

            for ( String word : invertedIndex.keySet() ) {
                bufferedWriter.write(word);
                bufferedWriter.write(":");

                for ( String docId : invertedIndex.get(word) ) {
                    bufferedWriter.write(docId);
                    bufferedWriter.write(' ');
                }
                bufferedWriter.write('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        closeClosable(bufferedWriter);

        System.out.println("Title index created ...");

    }

    public static void closeClosable(Closeable object) {
        try {
            if ( object != null )
                object.close();
        }
        catch (IOException e ){
            e.printStackTrace();
        }
    }
}
