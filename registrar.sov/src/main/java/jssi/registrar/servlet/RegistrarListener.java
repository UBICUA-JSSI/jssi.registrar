/*
 *
 *  * Copyright 2021 UBICUA.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
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
