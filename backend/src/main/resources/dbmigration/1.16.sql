-- apply alter tables
alter table cookbook_recipes alter column addition_date type timestamp using to_timestamp(addition_date);
alter table cookbook_users alter column join_date type timestamp using to_timestamp(join_date);
alter table cookbooks alter column creation_date type timestamp using to_timestamp(creation_date);
alter table follows alter column followed_since type timestamp using to_timestamp(followed_since);
alter table likes alter column creation_date type timestamp using to_timestamp(creation_date);
alter table recipes alter column creation_date type timestamp using to_timestamp(creation_date);
alter table recipes alter column modification_date type timestamp using to_timestamp(modification_date);
alter table users alter column token_end_validity type timestamp using to_timestamp(token_end_validity);
alter table users alter column join_date type timestamp using to_timestamp(join_date);
alter table users alter column last_activity_date type timestamp using to_timestamp(last_activity_date);
