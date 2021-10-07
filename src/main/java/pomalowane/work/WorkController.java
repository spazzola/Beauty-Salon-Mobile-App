package pomalowane.work;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(WorkController.class);

    @PostMapping("/create")
    public WorkDto createService(@RequestBody CreateWorkRequest createWorkRequest) {
        logger.info("Tworzenie uslugi: " + createWorkRequest);
        Work work = workService.createWork(createWorkRequest);
        logger.info("Utworzono usluge: " + work);

        return toDtoService.workToDto(work);
    }

    @PutMapping("/update")
    public WorkDto updateWork(@RequestBody WorkDto workDto) {
        logger.info("Aktualizowanie uslugi: " + workDto);
        Work work = workService.updateWork(workDto);
        logger.info("Zaktualizowano usluge: " + workDto);

        return toDtoService.workToDto(work);
    }

    @DeleteMapping("/delete")
    public HttpStatus deleteWork(@RequestParam Long id) {
        logger.info("Usuwanie uslugi o id: " + id);
        workService.deleteWork(id);
        logger.info("Usunieto usluge");

        return HttpStatus.OK;
    }

    @GetMapping("/getAll")
    public List<WorkDto> getAll() {
        List<Work> works = workService.getAll();

        return toDtoService.workToDto(works);
    }

}