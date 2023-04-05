package ai.openfabric.api.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class DockerService {

    public static String startDockerContainer(String name, Integer port) {

        DockerClient dockerClient = getDockerClient();
        String image = "nginx:latest";

        dockerClient.pullImageCmd(image).exec(new PullImageResultCallback()).awaitSuccess();

        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(image)
                .withName(name)
                .withExposedPorts(new ExposedPort(port))
                .exec();

        dockerClient.startContainerCmd(containerResponse.getId()).exec();

        return containerResponse.getId();
    }

    public static void stopDockerContainer(String containerId) {
        DockerClient dockerClient = getDockerClient();

        dockerClient.stopContainerCmd(containerId).exec();

        dockerClient.waitContainerCmd(containerId).exec(new WaitContainerResultCallback()).awaitStatusCode();
    }

    private static DockerClient getDockerClient() {
        String dockerHost = "tcp://localhost:2375";

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .build();

        return DockerClientImpl.getInstance(config);
    }

    public static String getDockerContainerStats(String containerId) {
        DockerClient dockerClient = getDockerClient();

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(containerId).exec();

        return containerResponse.toString();
    }
}