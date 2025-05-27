SELECT 
    EXTRACT(YEAR FROM invoice_date) AS year,
    EXTRACT(MONTH FROM invoice_date) AS month,
    TO_CHAR(invoice_date, 'YYYY-MM') AS year_month,
    COUNT(invoice_id) AS monthly_invoice_count,
    SUM(total_amount) AS monthly_revenue,
    AVG(total_amount) AS monthly_average_invoice,
    COUNT(DISTINCT customer_id) AS monthly_unique_customers
FROM 
    invoices
WHERE 
    DATE(invoice_date) BETWEEN :start_date AND :end_date
GROUP BY 
    EXTRACT(YEAR FROM invoice_date),
    EXTRACT(MONTH FROM invoice_date),
    TO_CHAR(invoice_date, 'YYYY-MM')
ORDER BY 
    year, month;