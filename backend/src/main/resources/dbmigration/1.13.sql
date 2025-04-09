-- apply alter tables
alter table ingredients add column if not exists name_en varchar(255) default '' not null;
alter table ingredients add column if not exists name_fr varchar(255) default '' not null;
