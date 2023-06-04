package discord.automation.runners;

import java.io.File;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

@Slf4j
@Testcontainers
public abstract class AbstractContainerRunner {

    private static final DockerComposeContainer<?> environment;

    static {
        environment = new DockerComposeContainer<>(getComposeFile())
//                .withLocalCompose(true)
//                .withOptions("--compatibility")
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
