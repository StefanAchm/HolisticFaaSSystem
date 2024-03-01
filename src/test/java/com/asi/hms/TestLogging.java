package com.asi.hms;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogging {

    private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);

    @Test
    void testLogging() {

        logger.info("This is an info message");
        logger.debug("This is a debug message");
        logger.error("This is an error message");
        logger.warn("This is a warning message");
        logger.trace("This is a trace message");

    }

}
