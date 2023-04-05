package ai.openfabric.api.controller;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping(path = "/workers")
    public ResponseEntity<Page<Worker>> listWorkers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Worker> workerPage = workerService.listWorkers(pageable);
        return ResponseEntity.ok(workerPage);
    }

    @PostMapping(path = "/start")
    public ResponseEntity<Worker> startWorker(@RequestParam("name") String name, @RequestParam("port") Integer port) {
        Worker worker = workerService.startWorker(name, port);
        return ResponseEntity.ok(worker);
    }

    @PostMapping(path = "/{id}/stop")
    public ResponseEntity<Worker> stopWorker(@PathVariable("id") String id) {
        Optional<Worker> optionalWorker = workerService.stopWorker(id);
        return optionalWorker.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Worker> getWorkerInfo(@PathVariable("id") String id) {
        Optional<Worker> optionalWorker = workerService.getWorkerInfo(id);
        return optionalWorker.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/stats")
    public ResponseEntity<String> getWorkerStats(@PathVariable("id") String id) {
        String stats = workerService.getWorkerStats(id);
        if (stats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }
}
