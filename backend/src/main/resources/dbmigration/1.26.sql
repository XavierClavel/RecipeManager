-- apply alter tables
alter table users alter column password_hash drop not null;
alter table users add column if not exists google_id varchar(255);
