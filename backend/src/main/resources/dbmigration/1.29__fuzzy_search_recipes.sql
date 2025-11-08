CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_recipe_name_trgm ON recipes USING GIST (title gist_trgm_ops);