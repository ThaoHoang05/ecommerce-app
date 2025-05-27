SELECT 
    COUNT(invoice_id) AS total_invoices,
    SUM(total_amount) AS total_revenue,
    AVG(total_amount) AS average_invoice_value,
    COUNT(DISTINCT customer_id) AS unique_customers,
    MIN(total_amount) AS min_invoice_value,
    MAX(total_amount) AS max_invoice_value
FROM 
    invoices
WHERE 
    DATE(invoice_date) BETWEEN :start_date AND :end_date;