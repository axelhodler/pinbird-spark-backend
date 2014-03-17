package org.xorrr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.xorrr.integrationtests.TestRestApi;

public class JsonAccessor {

    public static String getPostRequestBody() {
        String jsonString = null;

        try {
            File testFile = new File(TestRestApi.class.getResource(
                    "/linksPost.JSON").toURI());
            jsonString = IOUtils.toString(new FileInputStream(testFile));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static String getExampleLink() {
        String jsonString = null;

        try {
            File testFile = new File(TestRestApi.class.getResource(
                    "/link.json").toURI());
            jsonString = IOUtils.toString(new FileInputStream(testFile));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
