/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssi.registrar.servlet;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jssi.registrar.local.Drivers;

/**
 * Web application lifecycle listener.
 *
 * @author UBICUA
 */
public class RegistrarListener implements ServletContextListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(RegistrarListener.class);
    
    @Inject
    private Drivers drivers;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getInitParameter("jssi.registrar.config");
        try {
            drivers.init(path);
        } catch (IOException ex) {
            LOG.error(String.format("Initialization exception: %s", ex.getMessage()));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

