package com.stationeryshop.controller;

import com.stationeryshop.dao.ReportDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import javax.swing.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Controller class for handling report generation and data preparation
 * Coordinates between ReportDAO and JavaFX View components
 */
public class ReportController implements Initializable {

    // FXML injected components
    @FXML private Button generateReportBtn;
    @FXML private TextField customerSearchField;
    @FXML private DatePicker fromDatePicker;
    @FXML private CategoryAxis xAxis;
    @FXML private Label totalRevenueLabel;
    @FXML private BarChart<String, Number> revenueChart;
    @FXML private DatePicker toDatePicker;
    @FXML private PieChart productChart;
    @FXML private Label totalOrdersLabel;
    @FXML private Button clearSearchBtn;
    @FXML private TableView<Map<String, Object>> customerOrdersTable;
    @FXML private TableColumn<Map<String, Object>, String> orderIDColumn;
    @FXML private TableColumn<Map<String, Object>, String> customerPhoneColumn;
    @FXML private Label totalCustomersLabel;
    @FXML private NumberAxis yAxis;
    @FXML private Button searchCustomerBtn;
    @FXML private Button exportReportBtn;
    @FXML private TableColumn<Map<String, Object>, String> customerNameColumn;
    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private TableColumn<Map<String, Object>, String> customerEmailColumn;
    @FXML private TableColumn<Map<String, Object>, String> SpentColumn;
    @FXML private TableColumn<Map<String, Object>, String> customerIdColumn;

    // Business logic components
    private ReportDAO reportDAO;
    private DecimalFormat currencyFormatter;
    private DateTimeFormatter dateFormatter;
    private ObservableList<Map<String, Object>> reportData;

    // Report types
    private static final String DAILY_REVENUE = "Báo cáo doanh thu theo ngày";
    private static final String WEEKLY_REVENUE = "Báo cáo doanh thu theo tuần";
    private static final String MONTHLY_REVENUE = "Báo cáo doanh thu theo tháng";
    private static final String TOP_PRODUCTS = "Thống kê sản phẩm bán chạy";
    private static final String CUSTOMER_REPORT = "Báo cáo khách hàng";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDAO();
        initializeFormatters();
        initializeUI();
        setupTableColumns();
        setupReportTypes();
    }

    /**
     * Initialize DAO and formatters
     */
    private void initializeDAO() {
        this.reportDAO = new ReportDAO();
        this.currencyFormatter = new DecimalFormat("#,##0 VNĐ");
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.reportData = FXCollections.observableArrayList();
    }

    /**
     * Initialize formatters
     */
    private void initializeFormatters() {
        // Set default date range (last 30 days)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        fromDatePicker.setValue(startDate);
        toDatePicker.setValue(endDate);
    }

    /**
     * Initialize UI components
     */
    private void initializeUI() {
        // Initialize chart axes
        if (xAxis != null) {
            xAxis.setLabel("Thời gian");
        }
        if (yAxis != null) {
            yAxis.setLabel("Doanh thu (VNĐ)");
        }
        
        // Bind table data
        customerOrdersTable.setItems(reportData);
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        if (orderIDColumn != null) {
            orderIDColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    String.valueOf(cellData.getValue().get("invoiceId"))
                )
            );
        }
        
        if (customerNameColumn != null) {
            customerNameColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    (String) cellData.getValue().get("customerName")
                )
            );
        }
        
        if (customerPhoneColumn != null) {
            customerPhoneColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    (String) cellData.getValue().get("phoneNumber")
                )
            );
        }
        
        if (SpentColumn != null) {
            SpentColumn.setCellValueFactory(cellData -> {
                BigDecimal amount = (BigDecimal) cellData.getValue().get("totalAmount");
                return new javafx.beans.property.SimpleStringProperty(
                    formatCurrency(amount)
                );
            });
        }
    }

    /**
     * Setup report type combo box
     */
    private void setupReportTypes() {
        if (reportTypeComboBox != null) {
            reportTypeComboBox.setItems(FXCollections.observableArrayList(
                DAILY_REVENUE,
                WEEKLY_REVENUE,
                MONTHLY_REVENUE,
                TOP_PRODUCTS,
                CUSTOMER_REPORT
            ));
            reportTypeComboBox.setValue(DAILY_REVENUE);
        }
    }

    /**
     * Handle generate report button click
     */
    @FXML
    void handleGenerateReport(ActionEvent event) {
        try {
            String reportType = reportTypeComboBox.getValue();
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            if (fromDate == null || toDate == null) {
                showAlert("Lỗi", "Vui lòng chọn khoảng thời gian báo cáo", Alert.AlertType.ERROR);
                return;
            }

            if (fromDate.isAfter(toDate)) {
                showAlert("Lỗi", "Ngày bắt đầu không thể sau ngày kết thúc", Alert.AlertType.ERROR);
                return;
            }

            switch (reportType) {
                case DAILY_REVENUE:
                    generateDailyRevenueReport(fromDate, toDate);
                    break;
                case WEEKLY_REVENUE:
                    generateWeeklyRevenueReport(fromDate, toDate);
                    break;
                case MONTHLY_REVENUE:
                    generateMonthlyRevenueReport(fromDate, toDate);
                    break;
                case TOP_PRODUCTS:
                    generateTopProductsReport(fromDate, toDate);
                    break;
                case CUSTOMER_REPORT:
                    generateCustomerReport(fromDate, toDate);
                    break;
                default:
                    generateDailyRevenueReport(fromDate, toDate);
            }

        } catch (Exception e) {
            showAlert("Lỗi", "Có lỗi xảy ra khi tạo báo cáo: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Generate daily revenue report
     */
    private void generateDailyRevenueReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> dailyData = reportDAO.getDailyRevenueData(fromDate, toDate);
        
        // Update summary labels
        Map<String, Object> summary = reportDAO.getRevenueSummary(fromDate, toDate);
        updateSummaryLabels(summary);
        
        // Update table
        reportData.clear();
        reportData.addAll(dailyData);
        
        // Update bar chart
        updateRevenueChart(dailyData, "Doanh thu theo ngày");
        
        // Clear pie chart for revenue reports
        productChart.getData().clear();
    }

    /**
     * Generate weekly revenue report
     */
    private void generateWeeklyRevenueReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> weeklyData = aggregateWeeklyData(fromDate, toDate);
        
        // Update summary
        Map<String, Object> summary = reportDAO.getRevenueSummary(fromDate, toDate);
        updateSummaryLabels(summary);
        
        // Update table and chart
        reportData.clear();
        reportData.addAll(weeklyData);
        updateRevenueChart(weeklyData, "Doanh thu theo tuần");
        
        productChart.getData().clear();
    }

    /**
     * Generate monthly revenue report
     */
    private void generateMonthlyRevenueReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> monthlyData = aggregateMonthlyData(fromDate, toDate);
        
        Map<String, Object> summary = reportDAO.getRevenueSummary(fromDate, toDate);
        updateSummaryLabels(summary);
        
        reportData.clear();
        reportData.addAll(monthlyData);
        updateRevenueChart(monthlyData, "Doanh thu theo tháng");
        
        productChart.getData().clear();
    }

    /**
     * Generate top products report
     */
    private void generateTopProductsReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        // This would require additional methods in ReportDAO for product sales data
        // For now, showing placeholder implementation
        Map<String, Object> summary = reportDAO.getRevenueSummary(fromDate, toDate);
        updateSummaryLabels(summary);
        
        // Create sample pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Bút bi", 30),
            new PieChart.Data("Sổ tay", 25),
            new PieChart.Data("Bút chì", 20),
            new PieChart.Data("Tẩy", 15),
            new PieChart.Data("Khác", 10)
        );
        productChart.setData(pieChartData);
        productChart.setTitle("Top sản phẩm bán chạy");
        
        // Clear revenue chart
        revenueChart.getData().clear();
        reportData.clear();
    }

    /**
     * Generate customer report
     */
    private void generateCustomerReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> invoices = new ArrayList<>();
        
        // Get all invoices in date range
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            invoices.addAll(reportDAO.getInvoicesByDate(date));
        }
        
        Map<String, Object> summary = reportDAO.getRevenueSummary(fromDate, toDate);
        updateSummaryLabels(summary);
        
        reportData.clear();
        reportData.addAll(invoices);
        
        // Clear charts
        revenueChart.getData().clear();
        productChart.getData().clear();
    }

    /**
     * Handle search customer button click
     */
    @FXML
    void handleSearchCustomer(ActionEvent event) {
        String searchTerm = customerSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            showAlert("Thông báo", "Vui lòng nhập từ khóa tìm kiếm", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            // Filter current data based on search term
            ObservableList<Map<String, Object>> filteredData = FXCollections.observableArrayList();
            
            for (Map<String, Object> item : reportData) {
                String customerName = (String) item.get("customerName");
                String phoneNumber = (String) item.get("phoneNumber");
                
                if ((customerName != null && customerName.toLowerCase().contains(searchTerm.toLowerCase())) ||
                    (phoneNumber != null && phoneNumber.contains(searchTerm))) {
                    filteredData.add(item);
                }
            }
            
            customerOrdersTable.setItems(filteredData);
            
        } catch (Exception e) {
            showAlert("Lỗi", "Có lỗi xảy ra khi tìm kiếm: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle clear search button click
     */
    @FXML
    void handleClearSearch(ActionEvent event) {
        customerSearchField.clear();
        customerOrdersTable.setItems(reportData);
    }

    /**
     * Handle export report button click
     */
    @FXML
    void handleExportReport(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Xuất báo cáo");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            
            Stage stage = (Stage) exportReportBtn.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            
            if (file != null) {
                exportToCSV(file);
                showAlert("Thành công", "Xuất báo cáo thành công!", Alert.AlertType.INFORMATION);
            }
            
        } catch (Exception e) {
            showAlert("Lỗi", "Có lỗi xảy ra khi xuất báo cáo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Helper methods

    /**
     * Update summary labels
     */
    private void updateSummaryLabels(Map<String, Object> summary) {
        if (summary != null) {
            BigDecimal totalRevenue = (BigDecimal) summary.get("totalRevenue");
            Integer totalInvoices = (Integer) summary.get("totalInvoices");
            Integer uniqueCustomers = (Integer) summary.get("uniqueCustomers");
            
            totalRevenueLabel.setText(formatCurrency(totalRevenue != null ? totalRevenue : BigDecimal.ZERO));
            totalOrdersLabel.setText(String.valueOf(totalInvoices != null ? totalInvoices : 0));
            totalCustomersLabel.setText(String.valueOf(uniqueCustomers != null ? uniqueCustomers : 0));
        }
    }

    /**
     * Update revenue chart
     */
    private void updateRevenueChart(List<Map<String, Object>> data, String title) {
        revenueChart.getData().clear();
        revenueChart.setTitle(title);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
        
        for (Map<String, Object> item : data) {
            LocalDate date = (LocalDate) item.get("date");
            BigDecimal revenue = (BigDecimal) item.get("revenue");
            
            if (date != null && revenue != null) {
                series.getData().add(new XYChart.Data<>(
                    date.format(dateFormatter), 
                    revenue.doubleValue()
                ));
            }
        }
        
        revenueChart.getData().add(series);
    }

    /**
     * Aggregate daily data into weekly data
     */
    private List<Map<String, Object>> aggregateWeeklyData(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> dailyData = reportDAO.getDailyRevenueData(fromDate, toDate);
        List<Map<String, Object>> weeklyData = new ArrayList<>();
        
        // Group by week
        Map<String, BigDecimal> weeklyRevenue = new LinkedHashMap<>();
        Map<String, Integer> weeklyInvoices = new LinkedHashMap<>();
        
        for (Map<String, Object> daily : dailyData) {
            LocalDate date = (LocalDate) daily.get("date");
            BigDecimal revenue = (BigDecimal) daily.get("revenue");
            Integer invoiceCount = (Integer) daily.get("invoiceCount");
            
            // Calculate week start date (Monday)
            LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1);
            String weekKey = weekStart.format(dateFormatter);
            
            weeklyRevenue.merge(weekKey, revenue, BigDecimal::add);
            weeklyInvoices.merge(weekKey, invoiceCount, Integer::sum);
        }
        
        // Convert to list format
        for (Map.Entry<String, BigDecimal> entry : weeklyRevenue.entrySet()) {
            Map<String, Object> weekData = new HashMap<>();
            weekData.put("date", LocalDate.parse(entry.getKey(), dateFormatter));
            weekData.put("revenue", entry.getValue());
            weekData.put("invoiceCount", weeklyInvoices.get(entry.getKey()));
            weeklyData.add(weekData);
        }
        
        return weeklyData;
    }

    /**
     * Aggregate daily data into monthly data
     */
    private List<Map<String, Object>> aggregateMonthlyData(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<Map<String, Object>> dailyData = reportDAO.getDailyRevenueData(fromDate, toDate);
        List<Map<String, Object>> monthlyData = new ArrayList<>();
        
        // Group by month
        Map<String, BigDecimal> monthlyRevenue = new LinkedHashMap<>();
        Map<String, Integer> monthlyInvoices = new LinkedHashMap<>();
        
        for (Map<String, Object> daily : dailyData) {
            LocalDate date = (LocalDate) daily.get("date");
            BigDecimal revenue = (BigDecimal) daily.get("revenue");
            Integer invoiceCount = (Integer) daily.get("invoiceCount");
            
            String monthKey = date.getYear() + "-" + String.format("%02d", date.getMonthValue());
            
            monthlyRevenue.merge(monthKey, revenue, BigDecimal::add);
            monthlyInvoices.merge(monthKey, invoiceCount, Integer::sum);
        }
        
        // Convert to list format
        for (Map.Entry<String, BigDecimal> entry : monthlyRevenue.entrySet()) {
            Map<String, Object> monthData = new HashMap<>();
            String[] parts = entry.getKey().split("-");
            LocalDate monthDate = LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 1);
            monthData.put("date", monthDate);
            monthData.put("revenue", entry.getValue());
            monthData.put("invoiceCount", monthlyInvoices.get(entry.getKey()));
            monthlyData.add(monthData);
        }
        
        return monthlyData;
    }

    /**
     * Export current data to CSV
     */
    private void exportToCSV(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.append("Mã hóa đơn,Tên khách hàng,Số điện thoại,Tổng tiền,Ngày tạo\n");
            
            // Write data
            for (Map<String, Object> row : reportData) {
                writer.append(String.valueOf(row.get("invoiceId"))).append(",");
                writer.append(String.valueOf(row.get("customerName"))).append(",");
                writer.append(String.valueOf(row.get("phoneNumber"))).append(",");
                writer.append(String.valueOf(row.get("totalAmount"))).append(",");
                writer.append(String.valueOf(row.get("invoiceDate"))).append("\n");
            }
        }
    }

    /**
     * Format currency value
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0 VNĐ";
        }
        return currencyFormatter.format(amount);
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clean up resources
     */
}
