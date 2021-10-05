package pomalowane.work;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pomalowane.mappers.ToDtoService;

import java.util.List;

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

        return toDtoService.workToDto(work);
    }

    @PutMapping("/update")
    public WorkDto updateWork(@RequestBody WorkDto workDto) {
        Work work = workService.updateWork(workDto);

        return toDtoService.workToDto(work);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteWork(@RequestParam Long id) {
        workService.deleteWork(id);

        return HttpStatus.OK;
    }

    @GetMapping("/getAll")
    public List<WorkDto> getAll() {
        List<Work> works = workService.getAll();

        return toDtoService.workToDto(works);
    }

}