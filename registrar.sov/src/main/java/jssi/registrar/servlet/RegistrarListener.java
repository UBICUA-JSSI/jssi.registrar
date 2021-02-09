/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssi.registrar.servlet;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jssi.registrar.driver.sov.SovConfig;

/**
 * Web application lifecycle listener.
 *
 * @author UBICUA
 */
public class RegistrarListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrarListener.class);
    
    @Inject 
    SovConfig config;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String path = sce.getServletContext().getInitParameter("ubicua.driver.config");
        config.init(path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        config.closePools();
        config.closeWallet();
    }
}
