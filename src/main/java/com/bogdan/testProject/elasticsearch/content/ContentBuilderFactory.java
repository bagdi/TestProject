package com.bogdan.testProject.elasticsearch.content;

import org.elasticsearch.common.xcontent.XContentBuilder;

public class ContentBuilderFactory {

    private static ContentBuilderRus rus;

    private ContentBuilderFactory() {
        this.rus = ContentBuilderRus.getInstance();
    }

    public static XContentBuilder getXContentBuilderRus() {
        return ContentBuilderRus.getInstance().getXContentBuilderRu();
    }
}
