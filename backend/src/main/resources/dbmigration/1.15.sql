-- apply alter tables
alter table recipes add column if not exists tips varchar(511) default '' not null;
