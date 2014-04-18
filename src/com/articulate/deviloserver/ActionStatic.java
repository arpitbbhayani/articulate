package com.articulate.deviloserver;

/**
 * Created by devilo on 28/3/14.
 */

import java.io.*;

/**
 * Created by Arpit Bhayani on 7/1/14.
 */
public class ActionStatic implements ActionObject {

    @Override
    public void act(String requestURL, DataOutputStream outputStream, String httpVersion) {

        requestURL = requestURL.replaceFirst("/static/" , "");

        String filePath = new File(requestURL).getAbsolutePath();
        FileInputStream fileInputStream = null;

        StringBuffer response = new StringBuffer();

        try {

            fileInputStream = new FileInputStream(filePath);

        } catch (FileNotFoundException e) {
            try {
                response.append(HttpUtil.getHttpResponseHeader(404, "text/html", httpVersion));
                response.append("<!DOCTYPE html><html><head><title>Help Page</title></head><body style=\"margin: 30px; font-family:monospace; background-color: #eeeeee;\">");
                response.append("<h2>404 Not Found</h2>");
                outputStream.writeBytes(new String(response));
                return;

            } catch (IOException e1) {
                System.out.println("IOException : " + e1.getMessage());
                return;
            }
        }

        try {
            response.append(HttpUtil.getHttpResponseHeader(200, new File(filePath).toURL().openConnection().getContentType() , httpVersion));
            outputStream.writeBytes(new String(response));
        } catch (IOException e) {
            e.printStackTrace();
        }


        int readByte = 0;

        try {
            readByte = fileInputStream.read();
            while ( readByte != -1 ) {
                outputStream.write(readByte);
                readByte = fileInputStream.read();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if ( fileInputStream != null )
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }
}
