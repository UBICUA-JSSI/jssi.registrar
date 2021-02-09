/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jssi.registrar.local;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jssi.registrar.driver.http.HttpDriver;
import uniregistrar.driver.Driver;

/**
 *
 * @author UBICUA
 */
@ApplicationScoped
public class Drivers {

    private static final Logger LOG = LoggerFactory.getLogger(Drivers.class);

    private final Map<String, Driver> drivers = new HashMap<String, Driver>();

    public void init(String path) throws FileNotFoundException, IOException {

        final Gson gson = new Gson();

        try ( Reader reader = new FileReader(new File(path))) {

            JsonObject root = gson.fromJson(reader, JsonObject.class);
            JsonArray jsonArrayDrivers = root.getAsJsonArray("drivers");

            int i = 0;

            for (Iterator<JsonElement> jsonElementsDrivers = jsonArrayDrivers.iterator(); jsonElementsDrivers.hasNext();) {

                i++;

                JsonObject item = (JsonObject) jsonElementsDrivers.next();

                String id = item.has("id") ? item.get("id").getAsString() : null;
                String image = item.has("image") ? item.get("image").getAsString() : null;
                String imagePort = item.has("imagePort") ? item.get("imagePort").getAsString() : null;
                String imageProperties = item.has("imageProperties") ? item.get("imageProperties").getAsString() : null;
                String url = item.has("url") ? item.get("url").getAsString() : null;

                if (image == null && url == null) {
                    throw new IllegalArgumentException("Missing 'image' and 'url' entry in driver configuration (need either one).");
                }

                HttpDriver driver = new HttpDriver();

                if (url != null) {
                    driver.setRegisterUri(url + "1.0/register");
                    driver.setUpdateUri(url + "1.0/update");
                    driver.setDeactivateUri(url + "1.0/deactivate");
                } else {
                    String httpDriverUri = image.substring(image.indexOf("/"));
                    if (httpDriverUri.contains(":")) {
                        httpDriverUri = httpDriverUri.substring(0, httpDriverUri.indexOf(":"));
                    }
                    httpDriverUri = String.format("http://localhost:%s/%s/", (imagePort != null ? imagePort : "8080"), httpDriverUri);

                    driver.setRegisterUri(httpDriverUri + "1.0/register");
                    driver.setUpdateUri(httpDriverUri + "1.0/update");
                    driver.setDeactivateUri(httpDriverUri + "1.0/deactivate");

                    if ("true".equals(imageProperties)) {
                        driver.setPropertiesUri(httpDriverUri + "1.0/registrar/properties");
                    }

                    if (id == null) {
                        id = "driver";
                        if (image != null) {
                            id += "-" + image;
                        }
                        if (image == null || drivers.containsKey(id)) {
                            id += "-" + Integer.toString(i);
                        }
                    }

                    drivers.put(id, driver);

                    if (LOG.isInfoEnabled()) {
                        LOG.info("Added driver '" + id + "' at " + driver.getRegisterUri() + " (" + driver.getPropertiesUri() + ")");
                    }
                }
            }
        }
    }

    public Map<String, Driver> getDrivers() {
        return drivers;
    }

}
