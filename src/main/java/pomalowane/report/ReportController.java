package pomalowane.report;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {


    private ReportService reportService;


    @GetMapping("/generateMonthlyReport")
    public Report generateMonthlyReport(@RequestParam int month, @RequestParam int year) {
        return reportService.generateMonthlyReport(month, year);
    }

}