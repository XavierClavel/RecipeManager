-- drop dependencies
alter table recipes drop constraint if exists ck_recipes_dish_class;
-- apply post alter
alter table recipes add constraint ck_recipes_dish_class check ( dish_class in (0,1,2,3,4,5));
