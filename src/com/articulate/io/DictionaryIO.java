package com.articulate.io;

import com.articulate.util.HashedTrie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by devilo on 21/3/14.
 */
public class DictionaryIO {

    private HashedTrie nounDictionaryTrie = null;
    private HashedTrie otherDictionaryTrie = null;

    /**
     * Public constructor for initializing trie data structure
     * @param nounDictTrie : the trie data structure which stores nouns.
     * @param otherDictTrie : the trie data structure which stores other words.
     */
    public DictionaryIO(HashedTrie nounDictTrie, HashedTrie otherDictTrie) {
        this.nounDictionaryTrie = nounDictTrie;
        this.otherDictionaryTrie = otherDictTrie;
    }

    public void createTrie(String nounDictionaryPath, String otherDictionaryPath) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader= new BufferedReader(new FileReader(nounDictionaryPath));
            String line = null;
            while ( (line = bufferedReader.readLine()) != null ) {
                nounDictionaryTrie.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtil.closeClosable(bufferedReader);
        }

        try {
            bufferedReader= new BufferedReader(new FileReader(otherDictionaryPath));
            String line = null;
            while ( (line = bufferedReader.readLine()) != null ) {
                otherDictionaryTrie.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtil.closeClosable(bufferedReader);
        }
    }
}
