-- apply alter tables
alter table users add column if not exists mail_encrypted varchar(255) default '' not null;
alter table users add column if not exists mail_hash varchar(255) default '' not null;
