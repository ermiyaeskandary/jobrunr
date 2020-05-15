package org.jobrunr.tests.e2e;

import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.mongo.MongoDBStorageProvider;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;

public class MongoDBGsonBackgroundJobContainer extends AbstractBackgroundJobContainer {

    private final GenericContainer mongoContainer;

    public MongoDBGsonBackgroundJobContainer(GenericContainer mongoContainer) {
        super("jobrunr-e2e-mongo-gson:1.0");
        this.mongoContainer = mongoContainer;
    }

    @Override
    public void start() {
        Testcontainers.exposeHostPorts(mongoContainer.getFirstMappedPort());
        this
                .dependsOn(mongoContainer)
                .withEnv("MONGO_HOST", "host.testcontainers.internal")
                .withEnv("MONGO_PORT", String.valueOf(mongoContainer.getFirstMappedPort()));

        super.start();
    }

    @Override
    public StorageProvider getStorageProviderForClient() {
        return new MongoDBStorageProvider(mongoContainer.getContainerIpAddress(), mongoContainer.getFirstMappedPort());
    }
}
