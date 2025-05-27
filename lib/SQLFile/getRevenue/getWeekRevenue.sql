SELECT 
    EXTRACT(YEAR FROM invoice_date) AS year,
    EXTRACT(WEEK FROM invoice_date) AS week_number,
    DATE_TRUNC('week', invoice_date) AS week_start,
    COUNT(invoice_id) AS weekly_invoice_count,
    SUM(total_amount) AS weekly_revenue,
    AVG(total_amount) AS weekly_average_invoice,
    COUNT(DISTINCT customer_id) AS weekly_unique_customers
FROM 
    invoices
WHERE 
    DATE(invoice_date) BETWEEN :start_date AND :end_date
GROUP BY 
    EXTRACT(YEAR FROM invoice_date),
    EXTRACT(WEEK FROM invoice_date),
    DATE_TRUNC('week', invoice_date)
ORDER BY 
    year, week_number;