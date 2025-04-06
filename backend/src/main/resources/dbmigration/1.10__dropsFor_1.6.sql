-- apply alter tables
alter table cookbook_users drop column pending;
alter table cookbooks drop column is_public;
