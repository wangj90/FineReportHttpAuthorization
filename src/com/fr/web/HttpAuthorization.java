package com.fr.web;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpAuthorization extends HttpServlet implements Servlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("fr_username");
        String password = request.getParameter("fr_password");
        String url = "https://co-op.otiz.com.cn:34000/api/mHR/Login";
        Boolean result = requestLogin(url, "{\"UserID\":\"" + username + "\",\"PassWord\":\"" + password + "\",\"AppCode\":\"OTIZPortal\"}");
        response.getWriter().write(result.toString());
    }

    private static Boolean requestLogin(String url, String params) throws IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setEntity(new StringEntity(params, "utf-8"));
            try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
                HttpEntity httpEntity = response.getEntity();
                if (null != httpEntity) {
                    String str = EntityUtils.toString(httpEntity);
                    return str.startsWith("{\"code\":20000");
                }
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            closeableHttpClient.close();
        }
        return false;
    }
}
