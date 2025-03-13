package ncpl.bms.reports.service;
import ncpl.bms.reports.model.dto.MonthlyReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
@Service
public class MonthlyReportService {

    @Autowired
    private BillingService billingService;

    public List<MonthlyReportDTO> generateMonthlyReport(String month, String year) {
        List<MonthlyReportDTO> reportData = new ArrayList<>();

        // Fetch Tenant Details with Usage
        List<Map<String, Object>> tenants = billingService.getTenantsWithUsage(month, year);

        for (Map<String, Object> tenant : tenants) {
            MonthlyReportDTO report = new MonthlyReportDTO();
            report.setTenantName((String) tenant.get("name"));
            report.setUnitAddress((String) tenant.get("unit_address"));

            // Get EB and DG Usage (handle possible null values)
            double ebUsage = tenant.get("eb_usage") != null ? ((Number) tenant.get("eb_usage")).doubleValue() : 0.0;
            double dgUsage = tenant.get("dg_usage") != null ? ((Number) tenant.get("dg_usage")).doubleValue() : 0.0;

            // Get EB and DG Tariff (handle possible null values)
            double ebTariff = tenant.get("eb_tariff") != null ? ((Number) tenant.get("eb_tariff")).doubleValue() : 0.0;
            double dgTariff = tenant.get("dg_tariff") != null ? ((Number) tenant.get("dg_tariff")).doubleValue() : 0.0;

            // Get tenantId and isDeleted
            int tenantId = tenant.get("tenant_id") != null ? (int) tenant.get("tenant_id") : 0;
            int isDeleted = tenant.get("is_deleted") != null ? (int) tenant.get("is_deleted") : 0;

            // Compute Amounts
            report.setEbUsage(ebUsage);
            report.setDgUsage(dgUsage);
            report.setEbAmount(ebUsage * ebTariff);
            report.setDgAmount(dgUsage * dgTariff);
            report.setTotalAmount(report.getEbAmount() + report.getDgAmount());
            report.setTenantId(tenantId);
            report.setIsDeleted(isDeleted);

            reportData.add(report);
        }

        return reportData;
    }
}
