package ncpl.bms.reports.model.dto;

public class MonthlyReportDTO {
    private String tenantName;
    private String unitAddress;
    private double ebUsage;
    private int tenantId;
    private int isDeleted; // 1 means deleted, 0 means active

    private double dgUsage;
    private double ebAmount;
    private double dgAmount;
    private double totalAmount;

    public MonthlyReportDTO() {}

    public MonthlyReportDTO(String tenantName, String unitAddress, double ebUsage, double dgUsage,
                            double ebAmount, double dgAmount, double totalAmount, int tenantId, int isDeleted) {
        this.tenantName = tenantName;
        this.unitAddress = unitAddress;
        this.ebUsage = ebUsage;
        this.dgUsage = dgUsage;
        this.ebAmount = ebAmount;
        this.dgAmount = dgAmount;
        this.totalAmount = totalAmount;
        this.tenantId = tenantId;
        this.isDeleted = isDeleted;
    }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getUnitAddress() { return unitAddress; }
    public void setUnitAddress(String unitAddress) { this.unitAddress = unitAddress; }

    public double getEbUsage() { return ebUsage; }
    public void setEbUsage(double ebUsage) { this.ebUsage = ebUsage; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getIsDeleted() { return isDeleted; }
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }  // ✅ Fix added

    public double getDgUsage() { return dgUsage; }
    public void setDgUsage(double dgUsage) { this.dgUsage = dgUsage; }

    public double getEbAmount() { return ebAmount; }
    public void setEbAmount(double ebAmount) { this.ebAmount = ebAmount; }

    public double getDgAmount() { return dgAmount; }
    public void setDgAmount(double dgAmount) { this.dgAmount = dgAmount; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
