-- drop dependencies
alter table custom_ingredients drop constraint if exists ck_custom_ingredients_unit;
alter table recipe_ingredients drop constraint if exists ck_recipe_ingredients_unit;
-- apply post alter
alter table custom_ingredients add constraint ck_custom_ingredients_unit check ( unit in (0,1,2,3,4,5,6,7));
alter table recipe_ingredients add constraint ck_recipe_ingredients_unit check ( unit in (0,1,2,3,4,5,6,7));
