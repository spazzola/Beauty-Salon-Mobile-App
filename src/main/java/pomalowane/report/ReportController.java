package pomalowane.report;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import pomalowane.user.UserController;

@AllArgsConstructor
@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {


    private ReportService reportService;
    private static final Logger logger = LogManager.getLogger(ReportController.class);

    @GetMapping("/generateMonthlyReport")
    public Report generateMonthlyReport(@RequestParam int month, @RequestParam int year) {
        logger.info("Generowanie miesiecznego raportu: miesiac: " + month + ", rok: " + year);
        return reportService.generateMonthlyReport(month, year);
    }

}