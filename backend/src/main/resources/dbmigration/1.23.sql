-- apply alter tables
alter table ingredients add column if not exists carbohydrates float default 0 not null;
alter table ingredients add column if not exists saturated_fat float default 0 not null;
alter table ingredients add column if not exists unsaturated_fat float default 0 not null;
