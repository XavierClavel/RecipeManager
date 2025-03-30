-- apply alter tables
alter table users add column if not exists auto_accept_follow_requests boolean default false not null;
