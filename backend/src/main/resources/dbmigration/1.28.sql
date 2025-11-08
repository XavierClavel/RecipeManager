-- apply alter tables
alter table users add column if not exists locale integer default 0 not null;
-- apply post alter
alter table users add constraint ck_users_locale check ( locale in (0,1));
