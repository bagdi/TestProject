package com.bogdan.testProject.ogm.mongo;

import com.bogdan.testProject.model.Book;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;

public class MongoUtils {

    private Book book;

    public MongoUtils(Book book) {
        this.book = book;
    }

    public BasicDBObject toDBObject() {
        BasicDBObject document = new BasicDBObject();
        document.put("name", book.getName());
        document.put("list", book.getList());
        return document;
    }

    public static Book fromDBObject(DBObject document) {
        Book book = new Book();
        book.setName((String)document.get("name"));
        book.setList((List<String>)document.get("list"));
        return book;
    }
}
