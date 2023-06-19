package discord.automation.runners;

import static org.testcontainers.shaded.org.apache.commons.lang3.SystemUtils.*;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import javax.xml.datatype.DatatypeFactory;

@Slf4j
@Testcontainers
public abstract class AbstractContainerRunner {

    private static final DockerComposeContainer<?> environment;

    static {
        if (IS_OS_WINDOWS) {
            environment = new DockerComposeContainer<>(getComposeFile())
                    .withLocalCompose(true)
                    .withOptions("--compatibility");
        } else if (IS_OS_UNIX) {
            environment = new DockerComposeContainer<>(getComposeFile()).withLocalCompose(true);
        } else {
            throw new RuntimeException(String.format("Unknown os encountered: %s", OS_NAME));
        }
        environment
                .withLogConsumer("app", new Slf4jLogConsumer(log))
                .withLogConsumer("gitea", new Slf4jLogConsumer(log))
                .withLogConsumer("db", new Slf4jLogConsumer(log))
                .withStartupTimeout(Duration.ofSeconds(150))
                .waitingFor("app", Wait.forHealthcheck());
        Startables.deepStart(environment).join();
    }

    // TODO: Without this test db beans are being created without container being riced
    @Test
    void dummyTest() {}

    private static File getComposeFile() {
        final var file = Paths.get("target/docker/docker-compose.yml").toFile();
        log.info("Trying to open compose file with path: {}", file.getAbsolutePath());
        return file;
    }
}
