package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.FileNotFoundException;

public class FileUtilsTest extends BaseTestCase {

    @Test
    void testErrorPath() {
        FileUtils.readSingleFile("")
                .as(StepVerifier::create)
                .expectError(FileNotFoundException.class)
                .verify();
        FileUtils.readClassFile("asd.asd")
                .as(StepVerifier::create)
                .verifyComplete();
        FileUtils.readClassFile("asd.asd")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void testSinglePath() {
        FileUtils.readSingleClassFile("logback-test.xml")
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void testReadPath() {
        FileUtils.readClassFile("test")
                .count()
                .as(StepVerifier::create)
                .expectNext(2L)
                .verifyComplete();
    }

}
