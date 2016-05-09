package com.bogdan.testProject.io;

import java.util.List;

@FunctionalInterface
public interface ReadDocument {

    public List<String> read(String fileName);
}
