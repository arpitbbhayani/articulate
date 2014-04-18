package com.articulate.util;

/**
 * Created by devilo on 28/3/14.
 */
public class Article {

    String title = null;
    String body = null;

    public Article(String t, String b) {
        this.title = t;
        this.body = b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public  boolean isEmpty() {
        if ( (this.title == null || this.title.equals("")) && (this.body == null || this.body.equals(""))) {
            return true;
        }
        return false;
    }

}
