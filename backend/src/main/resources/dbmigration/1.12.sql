-- apply alter tables
alter table ingredients add column if not exists allow_amount boolean default true not null;
alter table ingredients add column if not exists allow_weight boolean default true not null;
alter table ingredients add column if not exists allow_volume boolean default true not null;
alter table ingredients add column if not exists volumic_mass float default 1.0 not null;
alter table ingredients add column if not exists weight_per_unit float default 1.0 not null;
