package cc.allio.uno.test.config;

import cc.allio.uno.test.TestComponentScan;
import cc.allio.uno.test.config.c1.C1Configuration;

@TestComponentScan(value = C1Configuration.class, basePackages = "cc.allio.uno.test.config")
public class TestConfiguration {
}
