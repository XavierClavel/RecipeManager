-- apply alter tables
alter table cookbooks add column if not exists visibility integer default 1 not null;
-- apply post alter
alter table cookbooks add constraint ck_cookbooks_visibility check ( visibility in (0,1,2));
