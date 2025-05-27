SELECT 
    COUNT(invoice_id) AS total_invoices,
    SUM(total_amount) AS total_revenue
FROM 
    invoices
WHERE 
    DATE(invoice_date) = :selected_date;