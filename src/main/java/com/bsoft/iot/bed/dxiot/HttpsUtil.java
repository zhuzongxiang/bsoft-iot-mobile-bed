package com.bsoft.iot.bed.dxiot;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class HttpsUtil extends DefaultHttpClient {
    public final static String HTTPGET = "GET";

    public final static String HTTPPUT = "PUT";

    public final static String HTTPPOST = "POST";

    public final static String HTTPDELETE = "DELETE";

    public final static String HTTPACCEPT = "Accept";

    public final static String CONTENT_LENGTH = "Content-Length";

    public final static String CHARSET_UTF8 = "UTF-8";

    public static String SELFCERTPATH = "/home/iot/hwcert/mycert/outgoing.CertwithKey.pkcs12";

    public static String SELFCERTPWD = "IoM@1234";

    public static String TRUSTCAPATH = "/home/iot/hwcert/mycert//ca.jks";

    public static String TRUSTCAPWD = "Huawei@123";

    private static HttpClient httpClient;

    public void initSSLConfigForTwoWay() throws Exception {
        KeyStore selfCert = KeyStore.getInstance("pkcs12");
        try {
            selfCert.load(new FileInputStream(SELFCERTPATH), SELFCERTPWD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
        kmf.init(selfCert, SELFCERTPWD.toCharArray());

        KeyStore caCert = KeyStore.getInstance("jks");
        try {
            caCert.load(new FileInputStream(TRUSTCAPATH), TRUSTCAPWD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(caCert);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        SSLSocketFactory ssf = new SSLSocketFactory(sc,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        ClientConnectionManager ccm = getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 8743, ssf));

        httpClient = new DefaultHttpClient(ccm);
    }

    public static HttpResponse doPost(String url, Map<String, String> headerMap,
                                      StringEntity stringEntity) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(stringEntity);

        return executeHttpRequest(request);
    }

    public static HttpResponse doPost(String url, Map<String, String> headerMap,
                                      InputStream inStream) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new InputStreamEntity(inStream));

        return executeHttpRequest(request);
    }

    public static HttpResponse doPostJson(String url,
                                          Map<String, String> headerMap, String content) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(
                new StringEntity(content, ContentType.APPLICATION_JSON));

        return executeHttpRequest(request);
    }

    public static String doPostJsonForString(String url,
                                      Map<String, String> headerMap, String content) {
        HttpPost request = new HttpPost(url);
        addRequestHeader(request, headerMap);

        request.setEntity(
                new StringEntity(content, ContentType.APPLICATION_JSON));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
        }
        System.out.println(((StreamClosedHttpResponse) response).getEntity());
        return ((StreamClosedHttpResponse) response).getContent();
    }

    public static String doPostJsonForString(String url, String content) {
        HttpPost request = new HttpPost(url);

        request.setEntity(
                new StringEntity(content, ContentType.APPLICATION_JSON));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return ((StreamClosedHttpResponse) response).getContent();
    }

    private static List<NameValuePair> paramsConverter(Map<String, String> params) {
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Map.Entry<String, String>> paramsSet = params.entrySet();
        for (Map.Entry<String, String> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(),
                    paramEntry.getValue()));
        }

        return nvps;
    }

    public static String doPostFormUrlEncodedForString(String url, Map<String, String> formParams)
            throws Exception {
        HttpPost request = new HttpPost(url);

        request.setEntity(new UrlEncodedFormEntity(paramsConverter(formParams)));

        HttpResponse response = executeHttpRequest(request);
        if (null == response) {
            System.out.println("The response body is null.");
            throw new Exception();
        }

        return ((StreamClosedHttpResponse) response).getContent();
    }

    public static HttpResponse doPut(String url, Map<String, String> headerMap, InputStream inStream) {
        HttpPut request = new HttpPut(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new InputStreamEntity(inStream));

        return executeHttpRequest(request);
    }

    public static HttpResponse doPutJson(String url,
                                         Map<String, String> headerMap, String content) {
        HttpPut request = new HttpPut(url);
        addRequestHeader(request, headerMap);

        request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));

        return executeHttpRequest(request);
    }

    public static String doPutJsonForString(String url,
                                     Map<String, String> headerMap, String content) {
        HttpResponse response = doPutJson(url, headerMap, content);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return ((StreamClosedHttpResponse) response).getContent();
    }

    public static HttpResponse doGet(String url, Map<String, String> headerMap) {
        HttpGet request = new HttpGet(url);
        addRequestHeader(request, headerMap);

        return executeHttpRequest(request);
    }

    public static HttpResponse doGetWithParas(String url, Map<String, String> queryParams, Map<String, String> headerMap)
            throws Exception {
        HttpGet request = new HttpGet();
        addRequestHeader(request, headerMap);

        URIBuilder builder;
        try {
            builder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            System.out.printf("URISyntaxException: {}", e);
            throw new Exception(e);

        }

        if (queryParams != null && !queryParams.isEmpty()) {
            builder.setParameters(paramsConverter(queryParams));
        }
        request.setURI(builder.build());

        return executeHttpRequest(request);
    }

    public static String doGetWithParasForString(String url, Map<String, String> mParam, Map<String, String> headerMap)
            throws Exception {
        HttpResponse response = doGetWithParas(url, mParam, headerMap);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return ((StreamClosedHttpResponse) response).getContent();
    }

    public static HttpResponse doDelete(String url,
                                        Map<String, String> headerMap) {
        HttpDelete request = new HttpDelete(url);
        addRequestHeader(request, headerMap);

        return executeHttpRequest(request);
    }

    public static String doDeleteForString(String url,
                                    Map<String, String> headerMap) {
        HttpResponse response = doDelete(url, headerMap);
        if (null == response) {
            System.out.println("The response body is null.");
        }

        return ((StreamClosedHttpResponse) response).getContent();
    }

    private static void addRequestHeader(HttpUriRequest request,
                                         Map<String, String> headerMap) {
        if (headerMap == null) {
            return;
        }

        for (String headerName : headerMap.keySet()) {
            if (CONTENT_LENGTH.equalsIgnoreCase(headerName)) {
                continue;
            }

            String headerValue = headerMap.get(headerName);
            request.addHeader(headerName, headerValue);
        }
    }

    private static HttpResponse executeHttpRequest(HttpUriRequest request) {
        HttpResponse response = null;

        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            System.out.println("executeHttpRequest failed.");
        } finally {
            try {
                response = new StreamClosedHttpResponse(response);
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }

        System.out.println("code "+ response.getStatusLine().getStatusCode());

        return response;
    }

    public static String getHttpResponseBody(HttpResponse response)
            throws UnsupportedOperationException, IOException {
        if (response == null) {
            return null;
        }

        String body = null;

        if (response instanceof StreamClosedHttpResponse) {
            body = ((StreamClosedHttpResponse) response).getContent();
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null && entity.isStreaming()) {
                String encoding = entity.getContentEncoding() != null
                        ? entity.getContentEncoding().getValue() : null;
                body = StreamUtil.inputStream2String(entity.getContent(),
                        encoding);
            }
        }

        return body;
    }
}
