-- apply alter tables
alter table users add column if not exists is_account_public boolean default false not null;
