package com.bogdan.testProject.model;

import java.util.List;

public class Book {

    private String name;
    private List<String> list;

    public Book(String name, List<String> list) {
        this.name = name;
        this.list = list;
    }

    public Book() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
