package com.articulate.deviloserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by devilo on 28/3/14.
 */
public class RequestThread extends Thread {

    private static final String GET_PREFIX = "GET";

    private static final String KEYWORD_HELP = "help";
    private static final String KEYWORD_ROOT = "/";
    private static final String KEYWORD_DB = "db";
    private static final String KEYWORD_STATIC = "static";

    BufferedReader inputStream;
    DataOutputStream outputStream;

    public RequestThread( BufferedReader inputStream , DataOutputStream outputStream ) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        handleHTTPRequestResponse(inputStream, outputStream);
    }

    /**
     *  Handles HTTP Request/Response
     */
    private void handleHTTPRequestResponse(BufferedReader inputStream , DataOutputStream outputStream ) {

        String request = null;

        try {
            request = inputStream.readLine();

            if ( request == null ) {
                String response = "NULL REQUEST\n";
                outputStream.writeBytes(response);
                outputStream.close();
                return;
            }

            if ( request.startsWith(GET_PREFIX) ) {

                String httpVersion = request.split(" ")[2];
                String requestURL = request.split(" ")[1];

                httpGETHandler( requestURL , outputStream , httpVersion);

                outputStream.close();

            }
            else {
                String response = "NON GET REQUEST\n";
                outputStream.writeBytes(response);
                outputStream.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * GET Handler
     * @param requestURL
     * @param outputStream
     * @param httpVersion
     */
    private void httpGETHandler(String requestURL, DataOutputStream outputStream, String httpVersion) throws IOException {

        /*
         *                 Table
         *      -----------------------------
         *      URL         |       Action
         *      -----------------------------
         *      /           |   Show help
         *      -----------------------------
         *      /help       |   Show Help
         *      -----------------------------
         *      /db         |   DB Table data
         *      -----------------------------
         *      /static     |   Show static file passed as URL
         *      -----------------------------
         */

        HashMap<String , ActionObject> httpGETActionMap = new HashMap<String, ActionObject>();

        //httpGETActionMap.put( KEYWORD_DB , new ActionDB());
        //httpGETActionMap.put( KEYWORD_ROOT , new ActionHELP());
        //httpGETActionMap.put( KEYWORD_HELP , new ActionHELP());
        httpGETActionMap.put( KEYWORD_STATIC , new ActionStatic());

        if ( requestURL.equals("/") ) {
            httpGETActionMap.get(requestURL).act(requestURL, outputStream , httpVersion);
            return;
        }

        String action = requestURL.split("/")[1];
        ActionObject actionObject = httpGETActionMap.get(action);

        if ( actionObject == null ) {
            String response = "Invalid REQUEST\n";
            outputStream.writeBytes(response);
            return;
        }
        actionObject.act(requestURL , outputStream , httpVersion);

    }
}