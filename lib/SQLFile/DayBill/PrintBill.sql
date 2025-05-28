SELECT 
    id.invoice_detail_id,
    id.invoice_id,
    id.product_id,
    p.product_name,
    p.product_code,
    id.quantity,
    id.unit_price,
    id.discount,
    id.line_total
FROM 
    invoice_details id
JOIN 
    products p ON id.product_id = p.product_id
WHERE 
    id.invoice_id = :invoice_id
ORDER BY 
    id.invoice_detail_id;