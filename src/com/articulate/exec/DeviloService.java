package com.articulate.exec;

import com.articulate.deviloserver.Server;

/**
 * Created by devilo on 28/3/14.
 */
public class DeviloService {
    public static void main(String[] args) {
        Server deviloServer = new Server(8011);
        deviloServer.start();
    }
}
