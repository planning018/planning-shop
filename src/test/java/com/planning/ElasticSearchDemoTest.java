package com.planning;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planning.config.EsConfig;
import com.planning.modules.app.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * elastic search tutorial
 */
@Slf4j
public class ElasticSearchDemoTest extends EsConfig {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Person insertPerson(Person person){
        person.setPersonId(UUID.randomUUID().toString());
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("personId",person.getPersonId());
        dataMap.put("name",person.getName());
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, person.getPersonId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
            log.info("insertPerson result: " + JSON.toJSONString(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }

    private static Person getPersonById(String id){
        GetRequest getPersonRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getPersonRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getResponse != null ?
                objectMapper.convertValue(getResponse.getSourceAsMap(), Person.class) : null;
    }

    private static Person updatePersonById(String id, Person person){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                // Fetch Object after its update
                .fetchSource(true);
        try {
            String personJson = objectMapper.writeValueAsString(person);
            updateRequest.doc(personJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("unable to update person");
        return null;
    }

    private static void deletePersonById(String id){
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        makeConnection();

        log.info("Insert a new Person with name Jack...");
        Person person = new Person();
        person.setName("Jack");
        person = insertPerson(person);
        log.info("Person inserted ---> " + person);

        log.info("Changing name to `Jack Ma`...");
        person.setName("Jack Ma");
        person = updatePersonById(person.getPersonId(), person);
        log.info("Person updated ---> " + person);

        log.info("Getting Jack Ma...");
        Person personFromDB = getPersonById(person.getPersonId());
        log.info("Person from DB ---> " + personFromDB);

/*        log.info("Deleting JackMa");
        deletePersonById("6910d596-142d-4205-90c2-08f72752fc34");
        log.info("Person Deleted");*/

        closeConnection();
    }


}