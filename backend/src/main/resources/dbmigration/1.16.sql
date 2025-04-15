-- apply alter tables
alter table cookbook_recipes alter column addition_date type timestamp using addition_date::timestamp;
alter table cookbook_users alter column join_date type timestamp using join_date::timestamp;
alter table cookbooks alter column creation_date type timestamp using creation_date::timestamp;
alter table follows alter column followed_since type timestamp using followed_since::timestamp;
alter table likes alter column creation_date type timestamp using creation_date::timestamp;
alter table recipes alter column creation_date type timestamp using creation_date::timestamp;
alter table recipes alter column modification_date type timestamp using modification_date::timestamp;
alter table users alter column token_end_validity type timestamp using token_end_validity::timestamp;
alter table users alter column join_date type timestamp using join_date::timestamp;
alter table users alter column last_activity_date type timestamp using last_activity_date::timestamp;
