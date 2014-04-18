package com.articulate.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by devilo on 29/3/14.
 */
public class NerOutputParser {

    private String inputFile = null;

    // entityMap to store the entities as
    // Category Name : Frequency (sorted order) : Entity Name
    private LinkedHashMap<String, TreeMap<Integer, ArrayList<String>>> entityMap = new LinkedHashMap<String, TreeMap<Integer, ArrayList<String>>>();

    public NerOutputParser(String s) {
        this.inputFile = s;
    }

    private void getFinalEntities() {
        String entityName = "";
        String entityType = "";
        int entityFreq = 0;
        TreeMap<Integer, ArrayList<String>> freqMap;
        try {
            FileInputStream fstream = new FileInputStream(inputFile);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String[] strArray;
            ArrayList<String> entityList;
            while ((strLine = br.readLine()) != null) {
                strArray = strLine.split("#");
                entityName = strArray[0].trim();
                entityType = strArray[1].trim();
                try {
                    entityFreq = Integer.parseInt(strArray[2].trim());
                } catch (Exception e) {
                    System.err.println("Error in file format at line : "
                            + strLine);
                    continue;
                }
                if (entityMap.containsKey(entityType)) {
                    freqMap = entityMap.get(entityType);
                    if (freqMap.containsKey(entityFreq)) {
                        entityList = freqMap.get(entityFreq);
                        entityList.add(entityName);
                        freqMap.put(entityFreq, entityList);
                        entityMap.put(entityType, freqMap);
                    } else {
                        entityList = new ArrayList<String>();
                        entityList.add(entityName);
                        freqMap.put(entityFreq, entityList);
                        entityMap.put(entityType, freqMap);
                    }

                } else {
                    freqMap = new TreeMap<Integer, ArrayList<String>>();
                    entityList = new ArrayList<String>();
                    entityList.add(entityName);
                    freqMap.put(entityFreq, entityList);
                    entityMap.put(entityType, freqMap);
                }

            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public LinkedHashMap<String , LinkedHashMap<String, Integer>> getEntityRepr() {

        List<String> listOfEntities = new ArrayList<String>();

        LinkedHashMap<String , LinkedHashMap<String, Integer>> mapi = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
        getFinalEntities();

        for ( String className : entityMap.keySet() ) {

            LinkedHashMap<String , Integer> mapii = new LinkedHashMap<String, Integer>();
            TreeMap<Integer , ArrayList<String>> list = entityMap.get(className);

            for ( Integer i : list.keySet() ) {
                ArrayList<String> entities = list.get(i);
                for ( String entity : entities ) {
                    mapii.put(entity , i);
                    listOfEntities.add(entity);
                }
            }

            mapi.put(className , mapii);

        }

        return mapi;


    }
}