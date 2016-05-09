package com.bogdan.testProject.ogm.mongo;

import com.bogdan.testProject.model.Book;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

public class BookSessionBean {

    private DBCollection collection;
    private MongoUtils utils;

    public BookSessionBean() {
        MongoClient mongo1 = new MongoClient("localhost", 27017);
        DB db = mongo1.getDB("book");
        collection = db.getCollection("books");
        if (collection == null) {
            collection = db.createCollection("books", null);
        }
    }


    public void createBook(Book book) {
        utils = new MongoUtils(book);
        BasicDBObject doc = utils.toDBObject();
        collection.insert(doc);
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            books.add(MongoUtils.fromDBObject(dbo));
        }
        return books;
    }
}
