package org.xorrr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.xorrr.integrationtests.TestRestApi;

public class JsonAccessor {

    public static String getPostRequestBody() {
        return fileToString("linksPost.JSON");
    }

    public static String getExampleLink() {
        return fileToString("link.json");
    }

    private static String fileToString(String filename) {
        String jsonString = null;

        try {
            File testFile = new File(TestRestApi.class.getResource(
                    "/" + filename).toURI());
            jsonString = IOUtils.toString(new FileInputStream(testFile));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
