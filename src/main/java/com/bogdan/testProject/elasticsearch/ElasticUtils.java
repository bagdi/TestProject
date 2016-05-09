package com.bogdan.testProject.elasticsearch;

import com.bogdan.testProject.ogm.mongo.BookSessionBean;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticUtils {

    private static final String BACKGROUND_COLOR = "FFFF00";

    public ElasticUtils() {
    }

    public static Map<String, String> putJsonDocument(List<String> list) {
        Map<String, String> map = new HashMap();

        for(int i = 0; i < list.size(); i++) {
            map.put("string" + i, list.get(i));
        }
        return map;
    }

    public static List<String> searchDocument(Client client, String index, String type, String field, String value) {
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.multiMatchQuery(value, field).operator(MatchQueryBuilder.Operator.AND))
                .addHighlightedField("string*", 400, 20)
                .setHighlighterPreTags("<span style='background-color:" + BACKGROUND_COLOR + "'>")
                .setHighlighterPostTags("</span>")
                .setSize(100)
                .execute()
                .actionGet();

        SearchHit[] hits = response.getHits().getHits();
        SearchHits searchHits = response.getHits();

        /*System.out.println("Total hits " + searchHits.getTotalHits());

        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> hightlight: hit.getHighlightFields().entrySet()) {
                for(Text text: hightlight.getValue().fragments()) {
                    System.out.println("Hightlight " + hightlight.getValue().getName() + text.string());
                }
            }
        }*/

        List<Integer> indexes;
        indexes = parseHighLightIndexes(searchHits, "string");
        List<Text> texts = new ArrayList<>();
        int i = 0;
        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> hightlight: hit.getHighlightFields().entrySet()) {
                for(Text text: hightlight.getValue().fragments()) {
                    texts.add(text);
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
                    //System.out.println(count + " " + texts.get(j).string());
                    count++;
                    continue out;
                }
            }
            newList.add(item);
            //System.out.println(count + " " + item);
            count++;
        }
        return newList;
    }

    public String strToUTF8(String value) {
        try {
            value = new String(value.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static List<Integer> parseHighLightIndexes(SearchHits hits, String idPrefixName) {
        List<Integer> list = new ArrayList<>();
        int idPostfixStartIndex = idPrefixName.length();
        for(SearchHit hit: hits) {
            for(Map.Entry<String, HighlightField> highlight: hit.getHighlightFields().entrySet()) {
                list.add(Integer.parseInt(highlight.getValue().getName().substring(idPostfixStartIndex)));
            }
        }
        return list;
    }
}
