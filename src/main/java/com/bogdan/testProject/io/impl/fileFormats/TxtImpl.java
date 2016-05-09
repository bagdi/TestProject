package com.bogdan.testProject.io.impl.fileFormats;

import com.bogdan.testProject.io.ReadDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TxtImpl implements ReadDocument {

    @Override
    public List<String> read(String fileName) {
        List<String> list = new ArrayList<>();
        try(Scanner scanner = new Scanner(new File(fileName))) {
            while(scanner.hasNext()) {
                list.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}
