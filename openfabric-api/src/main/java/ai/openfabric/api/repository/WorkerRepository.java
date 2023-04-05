package ai.openfabric.api.repository;

import ai.openfabric.api.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {

    @Modifying
    @Query("update Worker w set w.status = :status where w.id = :id")
    void updateWorkerStatus(@Param("id") String id, @Param("status") String status);

    int countByStatus(String status);
}
