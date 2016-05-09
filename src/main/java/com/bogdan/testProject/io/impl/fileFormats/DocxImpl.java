package com.bogdan.testProject.io.impl.fileFormats;

import com.bogdan.testProject.io.ReadDocument;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DocxImpl implements ReadDocument {

    @Override
    public List<String> read(String fileName) {
        List<String> paragraphs = new ArrayList<>();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);
            XHTMLOptions options = XHTMLOptions.create();
            OutputStream out = new FileOutputStream(new File("convert.html"));
            XHTMLConverter.getInstance().convert(document, out, options);
            XHTMLConverter.getInstance().convert(document, stream, options);
            String str = new String(stream.toByteArray(), "windows-1251");

            for(XWPFParagraph parItem: document.getParagraphs()) {
                paragraphs.add(parItem.getText());
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paragraphs;
    }

    public String readWithStyle(String fileName) {
        String str = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);
            XHTMLOptions options = XHTMLOptions.create();
            XHTMLConverter.getInstance().convert(document, stream, options);
            str = new String(stream.toByteArray(), "windows-1251");

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
