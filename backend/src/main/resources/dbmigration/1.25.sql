-- apply alter tables
alter table ingredients add column if not exists sugars float default 0 not null;
