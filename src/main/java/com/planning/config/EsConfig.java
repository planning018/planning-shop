package com.planning.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author planning
 * @since 2020-03-31 19:45
 **/
@Slf4j
public class EsConfig {

    /**
     * the config parameters for the connection
     */
    public static final String HOST = "114.215.125.55";
    private static final int PORT_ONE = 9200;
    private static final String SCHEME = "http";

    public static RestHighLevelClient restHighLevelClient;

    public static final String INDEX = "persondata";
    public static final String TYPE = "person";

    /**
     * Implements Singleton pattern here,
     * so that there is just one connection at a time.
     * @return RestHighLevelClient
     */
    public static synchronized RestHighLevelClient makeConnection(){
        if(restHighLevelClient == null){
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(HOST,PORT_ONE,SCHEME))
            );
        }
        return restHighLevelClient;
    }

    public static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

}