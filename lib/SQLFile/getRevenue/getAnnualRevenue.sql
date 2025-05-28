SELECT 
    EXTRACT(YEAR FROM invoice_date) AS year,
    COUNT(invoice_id) AS yearly_invoice_count,
    SUM(total_amount) AS yearly_revenue,
    AVG(total_amount) AS yearly_average_invoice,
    COUNT(DISTINCT customer_id) AS yearly_unique_customers
FROM 
    invoices
WHERE 
    DATE(invoice_date) BETWEEN :start_date AND :end_date
GROUP BY 
    EXTRACT(YEAR FROM invoice_date)
ORDER BY 
    year;