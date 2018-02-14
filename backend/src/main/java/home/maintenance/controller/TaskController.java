package home.maintenance.controller;

import com.fasterxml.jackson.annotation.JsonView;
import home.maintenance.dao.common.TaskRepository;
import home.maintenance.model.Task;
import home.maintenance.model.User;
import home.maintenance.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by vsoshyn on 25/10/2016.
 */
@RestController
@Secured("TASK_VIEW")
@RequestMapping(value = "/tasks", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @JsonView(Views.UserView.UI.class)
    @RequestMapping
    public List<Task> getTasks(@RequestParam("includeShared") boolean includeShared, @AuthenticationPrincipal User user) {
        return includeShared ? taskRepository.findAllByCreatedBy_IdOrShared(user.getId(), true) : taskRepository.findAllByCreatedBy_Id(user.getId());
    }

    @JsonView(Views.UserView.Name.class)
    @RequestMapping(value = "/shared")
    public List<Task> getSharedTasks() {
        return taskRepository.findAllByShared(true);
    }

    @Secured("TASK_MANAGEMENT")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createTask(@RequestBody Task task) {
        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }

    @Secured("TASK_MANAGEMENT")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTask(@PathVariable(name = "id") long id) {
        taskRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    @Secured("TASK_MANAGEMENT")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateTask(@RequestBody Task task) {
        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }

}
