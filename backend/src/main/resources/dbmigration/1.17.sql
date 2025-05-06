-- apply alter tables
alter table cookbooks add column if not exists image_version bigint default 0 not null;
alter table recipes add column if not exists image_version bigint default 0 not null;
alter table users add column if not exists image_version bigint default 0 not null;
