package org.xorrr.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEnvironmentVars {

    @Test
    public void envVarConstantsExist() {
        assertNotNull(EnvironmentVars.URI);
        assertNotNull(EnvironmentVars.DB_NAME);
        assertNotNull(EnvironmentVars.PW);
        assertNotNull(EnvironmentVars.PORT);
    }

}
