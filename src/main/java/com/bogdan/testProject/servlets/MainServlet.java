package com.bogdan.testProject.servlets;

import com.bogdan.testProject.elasticsearch.ClientDef;
import com.bogdan.testProject.elasticsearch.ElasticUtils;
import com.bogdan.testProject.ogm.mongo.BookSessionBean;
import com.google.gson.Gson;
import org.elasticsearch.client.Client;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainServlet extends HttpServlet {

    private final int stringCount = 50;
    private static int currentWindowCount;
    private static int prevWindowCount = 0;
    private List<String> list;
    private boolean flag;
    String searchString;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*final String FILE_NAME = "example.docx";
        readItem = new ReadDocumentFromFileSystemImpl<>(new DocxImpl());
        list = readItem.getT().read(FILE_NAME);*/
        list = getList();
        flag = true;
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("windows-1251");
        String searchButton = request.getParameter("searchButton");
        if(request.getParameter("searchButton") != null) {
            searchString = request.getParameter("searchLine");
        }
        currentWindowCount = Integer.parseInt(request.getParameter("currentWindowCount"));
        Gson gson = new Gson();
        String result;
        if(searchButton != null || flag == false) {
            flag = false;
            List<String> resultList = initClient(searchString);
            result = gson.toJson(next(resultList));
        } else {
            List<String> nextList = next(list);
            result = gson.toJson(nextList);
        }
        response.getWriter().write(result);
    }

    public List<String> next(List<String> stringList) {
        List<String> list = new ArrayList<>();
        prevWindowCount = currentWindowCount - 1;
        if(stringList.size() > (stringCount * currentWindowCount)) {
            for (int i = (stringCount * prevWindowCount); i < (stringCount * currentWindowCount); i++) {
                list.add(stringList.get(i));
            }
        } else {
            for (int i = (stringCount * prevWindowCount); i < stringList.size(); i++) {
                list.add(stringList.get(i));
            }
        }
        return list;
    }

    public List<String> getList() {
        BookSessionBean bean = new BookSessionBean();
        return bean.getBooks().get(0).getList();
    }

    public List<String> initClient(String searchString) {
        List<String> result;
        ClientDef def = new ClientDef("localhost", 9300);
        Client client = def.createClient();

        /*XContentBuilder settings = ContentBuilderFactory.getXContentBuilderRus();
        def.deleteClient(client);
        def.createIndex(client, settings);
        def.prepareIndex(list, client);*/

        ElasticUtils utils = new ElasticUtils();

        String value = utils.strToUTF8(searchString);

        result = utils.searchDocument(client, "book", "article", "string*", value);

        client.close();

        return result;
    }
}
