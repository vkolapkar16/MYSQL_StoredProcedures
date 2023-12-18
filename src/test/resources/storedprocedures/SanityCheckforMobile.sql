CREATE DEFINER=`root`@`localhost` PROCEDURE `SanityCheckforMobile`()
BEGIN
    SELECT 
        s1cc.*, 
        CONCAT(
-- Check Data Availability  
            CASE WHEN Vehicle_type IS NULL OR Vehicle_type = '' THEN 'Vehicle_type is null,' ELSE '' END,
            CASE WHEN Fuel_type IS NULL OR Fuel_type = '' THEN 'Fuel_type is null,' ELSE '' END,
            CASE WHEN Start_date IS NULL OR Start_date = '' THEN 'Start_date is null,' ELSE '' END,
            CASE WHEN End_date IS NULL OR End_date = '' THEN 'End_date is null,' ELSE '' END,
            CASE WHEN 
                    (Ghg IS NULL OR Ghg = '' OR Ghg_unit IS NULL OR Ghg_unit = '') AND
                    (Volume IS NULL OR Volume = '' OR Volume_unit IS NULL OR Volume_unit = '') AND
                    (Distance IS NULL OR Distance = '' OR Distance_unit IS NULL OR Distance_unit = '') AND
                    (Cost IS NULL OR Cost = '' OR Currency IS NULL OR Currency = '') 
                 THEN 'Emission fields are null.'
                 ELSE '' END,

-- Check Numeric values from fields
		CASE WHEN renewable IS NULL OR renewable = '' THEN ''
             WHEN NOT (renewable REGEXP '^[0-9,.-]+$') THEN 'renewable contains non-numeric data, '
		ELSE ''
        END,
        CASE WHEN Ghg IS NULL OR Ghg = '' THEN ''
              WHEN NOT (Ghg REGEXP '^[0-9,.-]+$') THEN 'Ghg contains non-numeric data, '
              ELSE ''
        END,
         CASE WHEN Volume IS NULL OR Volume = '' THEN ''
              WHEN NOT (Volume REGEXP '^[0-9,.-]+$') THEN 'Volume contains non-numeric data, '
              ELSE ''
        END,
        CASE WHEN Distance IS NULL OR Distance = '' THEN ''
              WHEN NOT (Distance REGEXP '^[0-9,.-]+$') THEN 'Distance contains non-numeric data, '
              ELSE ''
        END,
         CASE WHEN Cost IS NULL OR Cost = '' THEN ''
              WHEN NOT (Cost REGEXP '^[0-9,.-]+$') THEN 'Cost contains non-numeric data, '
              ELSE ''
        END
        ) AS Status
    FROM s1cc;
END