package com.waracle.cakemgr.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.waracle.cakemgr.CakeEntity;
import com.waracle.cakemgr.exceptions.CakeManagerException;
import com.waracle.cakemgr.service.CakeService;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;

public class DataLoader {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
    private static final String CAKES_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    public static void initAndLoadDataIntoDB(CakeService cakeService) throws ServletException {
        LOGGER.info("init started downloading cake json");
        HttpRestResult<Integer, String> cakesContent;

        try {
            cakesContent = HttpUtils.execute(new HttpGet(CAKES_URL));
        } catch (CakeManagerException e) {
            throw new ServletException(e);
        }

        LOGGER.info("parsing cake json");
        try (JsonParser parser = new JsonFactory().createParser(cakesContent.getResponse())) {
            if (JsonToken.START_ARRAY != parser.nextToken()) {
                throw new ServletException("bad token");
            }

            JsonToken nextToken = parser.nextToken();
            while (nextToken == JsonToken.START_OBJECT) {
                LOGGER.info("creating cake entity");

                CakeEntity cakeEntity = getCakeEntity(parser);
                LOGGER.info("Constructed cakeEntity {}", cakeEntity);
                cakeService.persist(cakeEntity);

                nextToken = parser.nextToken();
                LOGGER.info("nextToken {}", nextToken);
                nextToken = parser.nextToken();
                LOGGER.info("nextToken {}", nextToken);
                LOGGER.info("init finished");
            }
        } catch (IOException e) {
            LOGGER.error("Exception during JsonParser ", e);
            throw new ServletException(e);
        }
    }

    private static CakeEntity getCakeEntity(JsonParser jsonParser) throws IOException {
        CakeEntity cakeEntity = new CakeEntity();
        for(int i = 0; i < 3; i++) {
            String fieldName = jsonParser.nextFieldName();
            String value = jsonParser.nextTextValue();
            if(fieldName.equalsIgnoreCase("title")) {
                cakeEntity.setTitle(value);
            }else if(fieldName.equalsIgnoreCase("desc")) {
                cakeEntity.setDescription(value);
            }else if(fieldName.equalsIgnoreCase("image")) {
                cakeEntity.setImage(value);
            }
        }
        return cakeEntity;
    }
}
