DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'stock') THEN
      CREATE DATABASE stock;
   END IF;
END
$$;
