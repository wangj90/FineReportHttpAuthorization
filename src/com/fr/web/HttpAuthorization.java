package com.fr.web;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;

import javax.jws.WebService;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public class HttpAuthorization extends HttpServlet implements Servlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("fr_username");
        String password = request.getParameter("fr_password");
        String url = "http://10.31.8.43/UserAccount/Service1.asmx/Validate";
        Boolean result = doGetSoap1_1(url + "?name=" + URLEncoder.encode(username, "UTF-8") + "&pwd=" + URLEncoder.encode(password, "UTF-8"));
        response.getWriter().write(result.toString());
    }

    private static Boolean doGetSoap1_1(String url) {
        String retStr = "";
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            httpGet.setHeader("Content-Type", "text/xml;charset=UTF-8");
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (null != httpEntity) {
                InputStream in = httpEntity.getContent();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(in);
                retStr = document.getDocumentElement().getTextContent();
            }
            closeableHttpClient.close();
        } catch (Exception e) {
            return false;
        }
        return Boolean.valueOf(retStr);
    }
}
