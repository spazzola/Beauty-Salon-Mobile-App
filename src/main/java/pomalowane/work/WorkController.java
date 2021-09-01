package pomalowane.work;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

@AllArgsConstructor
@RestController
@RequestMapping("/work")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkController {

    private WorkService workService;
    private ToDtoService toDtoService;

    @PostMapping("/create")
    public WorkDto createService(@RequestBody CreateWorkRequest createWorkRequest) {
        Work work = workService.createWork(createWorkRequest);

        return toDtoService.toDto(work);
    }

}