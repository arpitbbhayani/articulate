package com.articulate.deviloserver;

/**
 * Created by devilo on 28/3/14.
 */
import java.io.IOException;

public class HttpUtil {

    public static String getHttpResponseHeader(int responseCode, String fileType, String httpVersion) throws IOException {

        /**
         *  The initial response line, called the status line, has three parts separated by spaces:
         *      1. the HTTP version,
         *      2. a response status code that gives the result of the request,
         *      3. and an English reason phrase describing the status code.
         *
         *  e.g. HTTP/1.0 200 OK
         *       HTTP/1.0 404 Not Found
         */

        StringBuffer response = new StringBuffer(httpVersion);

        switch ( responseCode ) {
            case 200:
                response.append(" 200 OK\r\n");
                break;
            case 404:
                response.append(" 404 Not Found\r\n");
                break;
            default:
                break;
        }

        if ( fileType != null )
            response.append("Content-Type: " + fileType + "\r\n");

        response.append("\r\n");

        return new String(response);
    }

    /**
     * This util funciton returns the SQL Query for a given URl
     * @param requestURL
     * @return SQL Query for a given URL.
     */
    public static String getSqlQueryFromURL(String requestURL) {

        String sqlQuery = "";

        /*
         *  URL for DB Query :
         *  /db/<databaseName>/<tableName>?where=<whereClause>
         *  [0] : null
         *  [1] : db
         *  [2] : databaseName
         *  [3] : <tableName>?where=<whereClause>
         */
        String[] pathTokens = requestURL.split("/");

        if ( pathTokens.length != 4 ) {
            return null;
        }

        String databaseName = pathTokens[2];
        String[] tableQuery = pathTokens[3].split("[?]");
        String tableName = tableQuery[0];
        String whereClause = "1 = 1";

        if ( tableQuery.length > 1 && tableQuery[1].startsWith("where=") ) {
            if ( tableQuery[1].indexOf('=') != -1 )
                whereClause = tableQuery[1].substring(tableQuery[1].indexOf('=') + 1);

            if ( whereClause.trim().equals("") ) {
                whereClause = "1 = 1";
            }
        }
        else if ( tableQuery.length > 1 ) {

            String[] param = tableQuery[1].split("[&]");

            if ( param.length == 1 ) {
                whereClause = tableQuery[1];
            }
            else if ( param.length > 1 ) {
                whereClause = "";
                for ( int i = 0 ; i < param.length ; i++ ) {
                    whereClause = whereClause + param[i];
                    if ( i != param.length - 1 )
                        whereClause = whereClause + " and ";
                }
            }

        }
        else {
            whereClause = "1 = 1";
        }

        sqlQuery = "select * from " + databaseName + "." + tableName + " where " + whereClause;

        return sqlQuery;
    }
}