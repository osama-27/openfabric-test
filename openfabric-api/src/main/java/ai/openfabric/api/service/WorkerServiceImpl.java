package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public Page<Worker> listWorkers(Pageable pageable) {
        return workerRepository.findAll(pageable);
    }

    @Override
    public Optional<Worker> getWorkerInfo(String id) {
        return workerRepository.findById(id);
    }

    @Override
    public Worker startWorker(String name, Integer port){
        String containerId = DockerService.startDockerContainer(name, port);
        Worker worker = new Worker();
        worker.setName(name);
        worker.setStatus("running");
        worker.setPort(port);
        worker.setContainerId(containerId);
        workerRepository.save(worker);
        return worker;
    }

    @Override
    public Optional<Worker> stopWorker(String id){
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (!optionalWorker.isPresent()) {
            return Optional.empty();
        }
        Worker worker = optionalWorker.get();
        DockerService.stopDockerContainer(worker.getContainerId());
        worker.setStatus("stopped");
        workerRepository.save(worker);
        return Optional.of(worker);
    }
    @Override
    public String getWorkerStats(String id){
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (!optionalWorker.isPresent()) {
            return "";
        }
        return DockerService.getDockerContainerStats(optionalWorker.get().getContainerId());
    }




}
