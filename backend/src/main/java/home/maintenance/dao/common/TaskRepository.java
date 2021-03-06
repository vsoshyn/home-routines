package home.maintenance.dao.common;

import home.maintenance.model.Task;
import home.maintenance.model.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCreatedBy_Id(long createdById);
    List<Task> findAllByCreatedBy_IdOrShared(long createdById, boolean shared);
    List<Task> findAllByShared(boolean shared);
    List<Task> findAllByState(TaskState state);
}
