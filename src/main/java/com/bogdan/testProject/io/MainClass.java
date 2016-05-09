package com.bogdan.testProject.io;

import com.bogdan.testProject.ogm.mongo.BookSessionBean;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClass {

    public static void main(String[] args) {
        /*final String FILE_NAME = "example.txt";
        ReadDocument readDocument = new TxtImpl();
        List<String> book = readDocument.read(FILE_NAME);*/
        BookSessionBean bean = new BookSessionBean();
        //bean.createBook(new Book("book", book));
        List<String> list = bean.getBooks().get(0).getList();

        int count = 0;
        for(String item: list) {
            System.out.println(count + " " + item);
            count++;
        }

        Client client = null;
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        /*XContentBuilder settings = getXContentBuilder();

        client.admin().indices().prepareDelete("book").get();

        long timeOutMill = 6000;
        client.admin().indices().create(new CreateIndexRequest("book").settings(settings)).actionGet(timeOutMill);

        client.prepareIndex("book", "article", "1").setSource(putJsonDocument(list)).get();*/

        String value = null;
        try {
            value = new String("машина".getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        searchDocument(client, "book", "article", "string*", value);

        client.close();
    }

    public static List<Integer> parseHighLightIndexes(SearchHits hits, String idPrefixName) {
        List<Integer> list = new ArrayList<>();
        int idPostfixStartIndex = idPrefixName.length();
        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> highlight: hit.getHighlightFields().entrySet()) {
                list.add(Integer.parseInt(highlight.getValue().getName().substring(idPostfixStartIndex)));
            }
        }
        return list;
    }

    public static Map<String, String> putJsonDocument(List<String> list) {
        Map<String, String> map = new HashMap();

        for(int i = 0; i < list.size(); i++) {
            map.put("string" + i, list.get(i));
        }
        return map;
    }

    public static void searchDocument(Client client, String index, String type, String field, String value) {
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.multiMatchQuery(value, field).operator(MatchQueryBuilder.Operator.AND))
                .addHighlightedField("string*", 200, 10)
                .setHighlighterPreTags("<span style='background-color':FFFF00>")
                .setHighlighterPostTags("</span>")
                .setSize(100)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        SearchHits searchHits = response.getHits();

        System.out.println("Total hits " + searchHits.getTotalHits());

        for(SearchHit hit: hits) {
            Map<String, Object> result = hit.getSource();
            System.out.println("Resource " + result);
            System.out.println(hit.getId());
            System.out.println("Resource as string " + hit.getSourceAsString());
        }

        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> hightlight: hit.getHighlightFields().entrySet()) {
                for(Text text: hightlight.getValue().fragments()) {
                    System.out.println("Hightlight " + hightlight.getValue().getName() + text.string());
                }
            }
        }

        List<Integer> indexes = parseHighLightIndexes(searchHits, "string");
        List<Text> texts = new ArrayList<>();
        int i = 0;
        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> hightlight: hit.getHighlightFields().entrySet()) {
                for(Text text: hightlight.getValue().fragments()) {
                    texts.add(text);
                    System.out.println("Hightlight " + indexes.get(i) + text.string());
                    i++;
                }
            }
        }

        BookSessionBean bean = new BookSessionBean();
        List<String> list = bean.getBooks().get(0).getList();

        List<String> newList = new ArrayList<>();
        int count = 0;
        out: for(String item: list) {
            for(int j = 0; j < indexes.size(); j++) {
                if (count == indexes.get(j)) {
                    newList.add(texts.get(j).string());
                    System.out.println(count + " " + texts.get(j).string());
                    count++;
                    continue out;
                }
            }
            newList.add(item);
            System.out.println(count + " " + item);
            count++;
        }
    }

    public static XContentBuilder getXContentBuilder() {
        XContentBuilder settings = null;
        try {
            settings = XContentFactory
                    .jsonBuilder()
                    .startObject()
                        .startObject("analysis")
                            .startObject("filter")
                                .startObject("russian_stop")
                                    .field("type", "stop").field("stopwords", "_russian_")
                                .endObject()
                                /*.startObject("russian_keywords")
                                    .field("type", "keyword_marker").startArray("keywords").endArray()
                                .endObject()*/
                                .startObject("russian_stemmer")
                                    .field("type", "stemmer").field("language", "russian")
                                .endObject()
                            .endObject()
                            .startObject("analyzer")
                                .startObject("russian")
                                    .field("tokenizer", "standard").field("filter", new String[] {"lowercase",
                                        "russian_stop",
                                        //"russian_keywords",
                                        "russian_stemmer"})
                                .endObject()
                                .startObject("default")
                                    .field("type", "russian")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }
}
