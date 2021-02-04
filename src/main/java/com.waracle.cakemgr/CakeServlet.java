package com.waracle.cakemgr;

import com.waracle.cakemgr.service.CakeService;
import com.waracle.cakemgr.service.CakeServiceImpl;
import com.waracle.cakemgr.utils.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/cakes"}, loadOnStartup = 1)
public class CakeServlet extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(CakeServlet.class);

    private CakeService cakeService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.cakeService = new CakeServiceImpl();
        DataLoader.initAndLoadDataIntoDB(this.cakeService);
        LOGGER.info("CakeServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CakeEntity> list = cakeService.findAll();
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
