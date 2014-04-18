package com.articulate.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devilo on 28/3/14.
 */
public class ArticulateServer {

    private int LIMIT = 5;
    private ArticulateNER articulateNER = null;

    public ArticulateServer () {
        articulateNER = new ArticulateNER();
        articulateNER.init();
    }

    public void start() {

        final Configuration articulateConfiguration = new Configuration();
        articulateConfiguration.setClassForTemplateLoading(ArticulateServer.class , "/resources");

        Spark.post(new Route("/analyze") {
            @Override
            public Object handle(Request request, Response response) {

                List<Article> listOfArticles = getListOfArticles(request);
                LinkedHashMap<String , LinkedHashMap<String , LinkedHashMap<String, Integer>>> map = articulateNER.processArticles(listOfArticles);

                try {
                    StringWriter documentWriter = new StringWriter();
                    Template template = articulateConfiguration.getTemplate("entity_analysis.ftl");
                    Map<String, Object> documentData = new LinkedHashMap<String, Object>();

                    documentData.put("map" , map);

                    template.process(documentData, documentWriter);
                    return documentWriter;

                } catch (IOException e) {
                    halt(500);
                    e.printStackTrace();
                } catch (TemplateException e) {
                    halt(500);
                    e.printStackTrace();
                }

                return null;
            }
        });


        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {

                try {

                    StringWriter documentWriter = new StringWriter();
                    Template template = articulateConfiguration.getTemplate("articulate_main_page.ftl");
                    Map<String, Object> documentData = new LinkedHashMap<String, Object>();
                    template.process(documentData, documentWriter);
                    return documentWriter;

                } catch (IOException e) {
                    halt(500);
                    e.printStackTrace();
                } catch (TemplateException e) {
                    halt(500);
                    e.printStackTrace();
                }

                return null;
            }

        });
    }


    /*****************************************************/
            /*
             *  Articulate Methods Used for getting final output
             */
    /******************************************************/

    private List<Article> getListOfArticles(Request request) {

        List<Article> articleList = new ArrayList<Article>();

        for ( int i = 1 ; i <= LIMIT ; i++ ) {
            String title = request.queryParams("article-title-"+i);
            String body = request.queryParams("article-body-"+i);

            if ( title == null && body == null )
                continue;

            if ( title == null )
                title = new String("");

            if ( body == null )
                body = new String("");


            Article article = new Article(title.replaceAll("\n", "") , body.replaceAll("\n" , ""));
            if ( !article.isEmpty() )
                articleList.add(article);

        }

        return articleList;

    }

}