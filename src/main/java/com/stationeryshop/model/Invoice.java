package com.stationeryshop.model;


import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private int invoiceId;
    private User user;                  // Người tạo hóa đơn
    private Customer customer;          // Khách hàng
    private LocalDate invoiceDate;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private String status;              // "pending", "completed"

    private List<InvoiceDetail> details;

    // Constructors
    public Invoice() {}

    public Invoice(int invoiceId, User user, Customer customer, LocalDate invoiceDate,
                   double totalAmount, double discountAmount, double finalAmount,
                   String status, List<InvoiceDetail> details) {
        this.invoiceId = invoiceId;
        this.user = user;
        this.customer = customer;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = status;
        this.details = details;
    }

    // Getters & Setters

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<InvoiceDetail> getDetails() { return details; }
    public void setDetails(List<InvoiceDetail> details) { this.details = details; }
}

