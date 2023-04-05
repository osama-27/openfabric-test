package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface WorkerService {
    Page<Worker> listWorkers(Pageable pageable);

    Optional<Worker> getWorkerInfo(String id);


    Worker startWorker(String name, Integer port);

    Optional<Worker> stopWorker(String id);

    String getWorkerStats(String id);
}