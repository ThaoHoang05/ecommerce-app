package com.stationeryshop.model;

public class InvoiceDetail {
    private int invoiceDetailId;
    private Invoice invoice;
    private Product product;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    // Constructors
    public InvoiceDetail() {}

    public InvoiceDetail(int invoiceDetailId, Invoice invoice, Product product,
                         int quantity, double unitPrice, double subtotal) {
        this.invoiceDetailId = invoiceDetailId;
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // Getters & Setters
    public int getInvoiceDetailId() { return invoiceDetailId; }
    public void setInvoiceDetailId(int invoiceDetailId) { this.invoiceDetailId = invoiceDetailId; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
