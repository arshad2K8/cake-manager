package com.waracle.cakemgr.utils;

import com.waracle.cakemgr.CakeServlet;
import com.waracle.cakemgr.exceptions.CakeManagerException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(CakeServlet.class);
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static HttpRestResult<Integer, String> execute(HttpUriRequest httpUriRequest) throws CakeManagerException {

        try(CloseableHttpResponse httpResponse = httpClient.execute(httpUriRequest)) {
            String response = EntityUtils.toString(httpResponse.getEntity());
            Integer responseCode = httpResponse.getStatusLine().getStatusCode();
            EntityUtils.consume(httpResponse.getEntity());
            return new HttpRestResult<>(responseCode, response);
        } catch (IOException e) {
            LOGGER.error("Exception while trying to execute request {}", httpUriRequest);
            throw new CakeManagerException(e);
        }
    }

    @Override
    public void finalize() {
        try {
            LOGGER.info("Closing httpClient now");
            httpClient.close();
        } catch (IOException e) {
            LOGGER.error("Exception trying to close httpClient", e);
        }
    }
}
