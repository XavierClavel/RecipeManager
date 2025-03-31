-- apply alter tables
alter table cookbook_users add column if not exists is_admin boolean default false not null;
