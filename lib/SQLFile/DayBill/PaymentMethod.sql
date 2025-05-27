SELECT 
    i.invoice_id,
    i.invoice_date,
    i.total_amount,
    i.payment_status,
    i.payment_method,
    c.customer_name,
    u.full_name AS staff_name
FROM 
    invoices i
LEFT JOIN 
    customers c ON i.customer_id = c.customer_id
LEFT JOIN 
    users u ON i.created_by = u.user_id
WHERE 
    DATE(i.invoice_date) = :selected_date
    AND i.payment_method = :payment_method
ORDER BY 
    i.invoice_date DESC;