package ncpl.bms.reports.controller;

import ncpl.bms.reports.model.dto.MonthlyReportDTO;
import ncpl.bms.reports.service.MonthlyReportPdfService;
import ncpl.bms.reports.service.MonthlyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("v1/monthly-report")
@CrossOrigin(origins = "http://localhost:4200")
public class MonthlyReportController {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyReportController.class);

    @Autowired
    private MonthlyReportService monthlyReportService;

    @Autowired
    private MonthlyReportPdfService monthlyReportPdfService;

    @GetMapping("/generate-bill")
    public ResponseEntity<byte[]> generateMonthlyBillPdf(@RequestParam("month") String month,
                                                         @RequestParam("year") String year) {
        logger.info("Generating monthly bill PDF for {}/{}", month, year);

        // ✅ Fetch all tenant reports
        List<MonthlyReportDTO> reportData = monthlyReportService.generateMonthlyReport(month, year);

        // ✅ Apply filtering to remove unwanted tenants
        List<MonthlyReportDTO> filteredReportData = reportData.stream()
                .filter(dto -> dto.getTenantId() != 1 && dto.getIsDeleted() == 0) // Exclude common area (1) & deleted tenants (isDeleted = 1)
                .toList();

        // ✅ If no valid tenants remain after filtering, return 204 (No Content)
        if (filteredReportData.isEmpty()) {
            logger.warn("No valid tenants found after filtering for {}/{}", month, year);
            return ResponseEntity.noContent().build();
        }

        // ✅ Generate PDF for filtered tenants only
        ByteArrayInputStream pdfStream = monthlyReportPdfService.generateBillPdf(filteredReportData, month, year);
        byte[] pdfBytes = pdfStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Monthly_Bill_" + month + "_" + year + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
