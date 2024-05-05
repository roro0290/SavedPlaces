package com.roro.random;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerBase {

    public static GenericContainer<?> mongoDbContainer
            = new GenericContainer("mongo:4.4.29")
            .withExposedPorts(27017);

    public static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDbContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDbContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "placesDb");
    }

    public static void startContainer() {
        mongoDbContainer.start();
    }

    public static void stopContainer() {
        mongoDbContainer.stop();
    }

}
