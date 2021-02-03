package com.waracle.cakemgr;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.waracle.cakemgr.exceptions.CakeManagerException;
import com.waracle.cakemgr.utils.HibernateUtil;
import com.waracle.cakemgr.utils.HttpRestResult;
import com.waracle.cakemgr.utils.HttpUtils;
import org.apache.http.client.methods.HttpGet;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = {"/", "/cakes"}, loadOnStartup = 1)
public class CakeServlet extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(CakeServlet.class);
    private static final String CAKES_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";
    @Override
    public void init() throws ServletException {
        super.init();
        LOGGER.info("init started");

        LOGGER.info("downloading cake json");

        /*
        try (InputStream inputStream = new URL(CAKES_URL).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            LOGGER.info("parsing cake json");
            JsonParser parser = new JsonFactory().createParser(buffer.toString());
            if (JsonToken.START_ARRAY != parser.nextToken()) {
                throw new Exception("bad token");
            }

            JsonToken nextToken = parser.nextToken();
            while(nextToken == JsonToken.START_OBJECT) {
                LOGGER.info("creating cake entity");

                CakeEntity cakeEntity = new CakeEntity();
                cakeEntity.setTitle(parser.nextTextValue());
                cakeEntity.setDescription(parser.nextTextValue());
                cakeEntity.setImage(parser.nextTextValue());
                LOGGER.info("Constructed cakeEntity is {}", cakeEntity);
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    session.beginTransaction();
                    session.persist(cakeEntity);
                    LOGGER.info("adding cake entity");
                    session.getTransaction().commit();
                } catch (ConstraintViolationException ex) {

                }
                session.close();

                nextToken = parser.nextToken();
                System.out.println(nextToken);

                nextToken = parser.nextToken();
                System.out.println(nextToken);
            }

        } catch (Exception ex) {
            throw new ServletException(ex);
        } */

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

                CakeEntity cakeEntity = new CakeEntity();
                for(int i = 0; i < 3; i++) {
                    String fieldName = parser.nextFieldName();
                    String value = parser.nextTextValue();
                    if(fieldName.equalsIgnoreCase("title")) {
                        cakeEntity.setTitle(value);
                    }else if(fieldName.equalsIgnoreCase("desc")) {
                        cakeEntity.setDescription(value);
                    }else if(fieldName.equalsIgnoreCase("image")) {
                        cakeEntity.setImage(value);
                    }
                }

                //cakeEntity.setDescription(parser.nextTextValue());
                //cakeEntity.setImage(parser.nextTextValue());
                LOGGER.info("Constructed cakeEntity is {}", cakeEntity);
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    session.beginTransaction();
                    session.persist(cakeEntity);
                    LOGGER.info("adding cake entity");
                    session.getTransaction().commit();
                } catch (ConstraintViolationException ex) {
                    LOGGER.error("ConstraintViolationException ", ex);
                }
                session.close();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();

        resp.getWriter().println("[");

        for (CakeEntity entity : list) {
            resp.getWriter().println("\t{");

            resp.getWriter().println("\t\t\"title\" : " + entity.getTitle() + ", ");
            resp.getWriter().println("\t\t\"desc\" : " + entity.getDescription() + ",");
            resp.getWriter().println("\t\t\"image\" : " + entity.getImage());

            resp.getWriter().println("\t}");
        }

        resp.getWriter().println("]");

    }


}
