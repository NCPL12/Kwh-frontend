package ncpl.bms.reports.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;

import ncpl.bms.reports.model.dto.MonthlyReportDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyReportPdfService {
    private static final Logger logger = LoggerFactory.getLogger(MonthlyReportPdfService.class);

    public ByteArrayInputStream generateBillPdf(List<MonthlyReportDTO> reportData, String month, String year) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Filter out tenantId = 1 and tenants where is_deleted = 1
            List<MonthlyReportDTO> filteredReportData = reportData.stream()
                    .filter(dto -> dto.getTenantId() != 1 && dto.getIsDeleted() == 0)
                    .collect(Collectors.toList());

            // **Company Header**
            Paragraph companyName = new Paragraph("Vajram CMR One")
                    .setFontSize(14)
                    .setBold()
                    .setMarginBottom(10)
                    .setFixedPosition(30, pdf.getDefaultPageSize().getHeight() - 40, 200);
            document.add(companyName);

            // Add the logo in the top-right corner
            try {
                ImageData logoData = ImageDataFactory.create(new ClassPathResource("static/images/logo1.png").getURL());
                Image logo = new Image(logoData);
                logo.scaleToFit(100, 100);

                float pageWidth = pdf.getDefaultPageSize().getWidth();
                float pageHeight = pdf.getDefaultPageSize().getHeight();
                float marginX = 20;
                float marginY = 10;
                float logoX = pageWidth - logo.getImageScaledWidth() - marginX;
                float logoY = pageHeight - logo.getImageScaledHeight() - marginY;

                logo.setFixedPosition(logoX, logoY);
                document.add(logo);
            } catch (Exception e) {
                throw new RuntimeException("Error loading logo image", e);
            }

            // **Report Title**
            Paragraph companyHeader = new Paragraph("Monthly Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16);
            document.add(companyHeader);

            // **Billing Month and Date Formatting**
            DateTimeFormatter billingMonthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // Format: February 2024
            DateTimeFormatter generatedDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Format: 13-03-2025

            String formattedBillingMonth = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1).format(billingMonthFormatter);
            String formattedGeneratedOn = LocalDate.now().format(generatedDateFormatter);

            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth()
                    .setMarginTop(10)
                    .setBorder(Border.NO_BORDER);

            headerTable.addCell(new Cell().add(new Paragraph("Billing Month: " + formattedBillingMonth)
                            .setBold().setFontSize(12))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER));

            headerTable.addCell(new Cell().add(new Paragraph("Generated on: " + formattedGeneratedOn)
                            .setFontSize(10))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            document.add(headerTable);

            // **Table Setup with Formatting**
            float[] columnWidths = {5, 4};
            Table table = new Table(UnitValue.createPercentArray(columnWidths))
                    .useAllAvailableWidth()
                    .setMarginTop(10)
                    .setBorder(new SolidBorder(1));

            // **Table Headers**
            Cell tenantHeader = new Cell().add(new Paragraph("Floor Name").setBold().setFontSize(10))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            table.addHeaderCell(tenantHeader);

            Cell usageHeader = new Cell().add(new Paragraph("EB Usage (kWh)").setBold().setFontSize(10))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            table.addHeaderCell(usageHeader);

            // **Table Data Rows**
            for (MonthlyReportDTO dto : filteredReportData) {
                table.addCell(new Cell().add(new Paragraph(dto.getTenantName()))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setPadding(5));

                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", dto.getEbUsage())))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setPadding(5));
            }

            document.add(table);

            // **Footer**
            float pageBottomMargin = 15;
            float pageRightMargin = 30;
            PdfFont italicFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

            Paragraph footer = new Paragraph("Report generated by Neptune Control Pvt Ltd")
                    .setFont(italicFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFixedPosition(pdf.getDefaultPageSize().getWidth() - pageRightMargin - 180, pageBottomMargin, 180);

            document.add(footer);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            logger.error("Error generating monthly report PDF", e);
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
