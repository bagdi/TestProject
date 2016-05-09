package com.bogdan.testProject.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ClientDef {

    private String host;
    private int port;

    public ClientDef(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void createIndex(Client client, XContentBuilder settings) {
        long timeOutMill = 6000;
        client.admin().indices().create(new CreateIndexRequest("book").settings(settings))
                .actionGet(timeOutMill);
    }

    public Client createClient() {
        Client client = null;
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    public void deleteClient(Client client) {
        client.admin().indices().prepareDelete("book").get();
    }

    public void prepareIndex(List<String> list, Client client) {
        client.prepareIndex("book", "article", "1").setSource(ElasticUtils.putJsonDocument(list)).get();
    }
}
