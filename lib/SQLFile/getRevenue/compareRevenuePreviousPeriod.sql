SELECT 
    current_period.period_label,
    current_period.current_revenue,
    current_period.current_invoices,
    COALESCE(previous_period.previous_revenue, 0) AS previous_revenue,
    COALESCE(previous_period.previous_invoices, 0) AS previous_invoices,
    CASE 
        WHEN COALESCE(previous_period.previous_revenue, 0) > 0 THEN
            ROUND(((current_period.current_revenue - COALESCE(previous_period.previous_revenue, 0)) 
                   / COALESCE(previous_period.previous_revenue, 0) * 100), 2)
        ELSE 0
    END AS revenue_growth_percentage
FROM (
    SELECT 
        TO_CHAR(invoice_date, 'YYYY-MM') AS period_label,
        SUM(total_amount) AS current_revenue,
        COUNT(invoice_id) AS current_invoices
    FROM invoices
    WHERE DATE(invoice_date) BETWEEN :start_date AND :end_date
    GROUP BY TO_CHAR(invoice_date, 'YYYY-MM')
) current_period
LEFT JOIN (
    SELECT 
        TO_CHAR(invoice_date, 'YYYY-MM') AS period_label,
        SUM(total_amount) AS previous_revenue,
        COUNT(invoice_id) AS previous_invoices
    FROM invoices
    WHERE DATE(invoice_date) BETWEEN :previous_start_date AND :previous_end_date
    GROUP BY TO_CHAR(invoice_date, 'YYYY-MM')
) previous_period ON 
    SUBSTRING(current_period.period_label, 6) = SUBSTRING(previous_period.period_label, 6)
ORDER BY current_period.period_label;