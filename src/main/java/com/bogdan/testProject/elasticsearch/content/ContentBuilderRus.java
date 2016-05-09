package com.bogdan.testProject.elasticsearch.content;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class ContentBuilderRus {

    private static ContentBuilderRus rus = null;

    private ContentBuilderRus() {
    }

    protected XContentBuilder getXContentBuilderRu() {
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

    public static ContentBuilderRus getInstance() {
        if(rus == null) {
            rus = new ContentBuilderRus();
        }
        return rus;
    }
}
