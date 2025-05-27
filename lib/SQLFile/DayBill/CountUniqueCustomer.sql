SELECT 
    COUNT(DISTINCT customer_id) AS unique_customers
FROM 
    invoices
WHERE 
    DATE(invoice_date) = :selected_date
    AND customer_id IS NOT NULL;