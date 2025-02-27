-- apply changes
create table cookbooks (
  id                            bigint generated by default as identity not null,
  creation_date                 bigint not null,
  is_public                     boolean default false not null,
  title                         varchar(255) not null,
  description                   varchar(255) not null,
  constraint pk_cookbooks primary key (id)
);

create table cookbook_recipes (
  id                            bigint generated by default as identity not null,
  recipe_id                     bigint not null,
  added_by_id                   bigint not null,
  cookbook_id                   bigint not null,
  addition_date                 bigint not null,
  constraint pk_cookbook_recipes primary key (id)
);

create table cookbook_users (
  id                            bigint generated by default as identity not null,
  user_id                       bigint not null,
  cookbook_id                   bigint not null,
  role                          integer not null,
  pending                       boolean default false not null,
  join_date                     bigint not null,
  constraint ck_cookbook_users_role check ( role in (0,1,2)),
  constraint pk_cookbook_users primary key (id)
);

create table custom_ingredients (
  id                            bigint generated by default as identity not null,
  recipe_id                     bigint,
  amount                        float,
  unit                          integer not null,
  name                          varchar(255) not null,
  constraint ck_custom_ingredients_unit check ( unit in (0,1,2,3,4,5,6)),
  constraint pk_custom_ingredients primary key (id)
);

create table dietary_restrictions (
  id                            bigint generated by default as identity not null,
  is_meat_ok                    boolean default false not null,
  is_fish_ok                    boolean default false not null,
  is_egg_ok                     boolean default false not null,
  is_alcohol_ok                 boolean default false not null,
  is_pork_ok                    boolean default false not null,
  is_gluten_ok                  boolean default false not null,
  is_sugar_ok                   boolean default false not null,
  constraint pk_dietary_restrictions primary key (id)
);

create table follows (
  id                            bigint generated by default as identity not null,
  follower_id                   bigint,
  user_id                       bigint,
  pending                       boolean default false not null,
  followed_since                bigint not null,
  constraint pk_follows primary key (id)
);

create table ingredients (
  id                            bigint generated by default as identity not null,
  type                          integer not null,
  calories                      integer not null,
  glucids                       float not null,
  cholesterol                   float not null,
  lipids                        float not null,
  fibers                        float not null,
  proteins                      float not null,
  sodium                        float not null,
  name                          varchar(255) not null,
  constraint ck_ingredients_type check ( type in (0,1,2,3,4,5,6,7,8,9,10,11,12)),
  constraint pk_ingredients primary key (id)
);

create table likes (
  id                            bigint generated by default as identity not null,
  user_id                       bigint not null,
  recipe_id                     bigint not null,
  creation_date                 bigint not null,
  constraint pk_likes primary key (id)
);

create table recipes (
  id                            bigint generated by default as identity not null,
  dish_class                    integer not null,
  creation_date                 bigint not null,
  modification_date             bigint not null,
  yield                         integer,
  preparation_time              integer,
  cooking_time                  integer,
  cooking_temperature           integer,
  conservation_time             integer,
  owner_id                      bigint,
  tagged_for_deletion           boolean default false not null,
  title                         varchar(255) not null,
  description                   varchar(255) not null,
  constraint ck_recipes_dish_class check ( dish_class in (0,1,2,3)),
  constraint pk_recipes primary key (id)
);

create table recipes_steps (
  recipes_id                    bigint not null,
  value                         varchar(255) not null
);

create table recipe_ingredients (
  id                            bigint generated by default as identity not null,
  recipe_id                     bigint,
  ingredient_id                 bigint,
  amount                        float,
  unit                          integer not null,
  complement                    varchar(255),
  constraint ck_recipe_ingredients_unit check ( unit in (0,1,2,3,4,5,6)),
  constraint pk_recipe_ingredients primary key (id)
);

create table users (
  id                            bigint generated by default as identity not null,
  role                          integer not null,
  token_end_validity            bigint not null,
  is_verified                   boolean default false not null,
  account_visibility            integer not null,
  auto_accept_follow_requestss  boolean default false not null,
  join_date                     bigint not null,
  last_activity_date            bigint not null,
  dietary_restrictions_id       bigint not null,
  username                      varchar(255) not null,
  mail                          varchar(255) not null,
  password_hash                 varchar(255) not null,
  bio                           varchar(255) not null,
  token                         varchar(255) not null,
  constraint ck_users_role check ( role in (0,1)),
  constraint ck_users_account_visibility check ( account_visibility in (0,1,2)),
  constraint uq_users_username unique (username),
  constraint uq_users_mail unique (mail),
  constraint uq_users_token unique (token),
  constraint uq_users_dietary_restrictions_id unique (dietary_restrictions_id),
  constraint pk_users primary key (id)
);

-- foreign keys and indices
create index ix_cookbook_recipes_recipe_id on cookbook_recipes (recipe_id);
alter table cookbook_recipes add constraint fk_cookbook_recipes_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_cookbook_recipes_added_by_id on cookbook_recipes (added_by_id);
alter table cookbook_recipes add constraint fk_cookbook_recipes_added_by_id foreign key (added_by_id) references users (id) on delete restrict on update restrict;

create index ix_cookbook_recipes_cookbook_id on cookbook_recipes (cookbook_id);
alter table cookbook_recipes add constraint fk_cookbook_recipes_cookbook_id foreign key (cookbook_id) references cookbooks (id) on delete restrict on update restrict;

create index ix_cookbook_users_user_id on cookbook_users (user_id);
alter table cookbook_users add constraint fk_cookbook_users_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_cookbook_users_cookbook_id on cookbook_users (cookbook_id);
alter table cookbook_users add constraint fk_cookbook_users_cookbook_id foreign key (cookbook_id) references cookbooks (id) on delete restrict on update restrict;

create index ix_custom_ingredients_recipe_id on custom_ingredients (recipe_id);
alter table custom_ingredients add constraint fk_custom_ingredients_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_follows_follower_id on follows (follower_id);
alter table follows add constraint fk_follows_follower_id foreign key (follower_id) references users (id) on delete restrict on update restrict;

create index ix_follows_user_id on follows (user_id);
alter table follows add constraint fk_follows_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_likes_user_id on likes (user_id);
alter table likes add constraint fk_likes_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_likes_recipe_id on likes (recipe_id);
alter table likes add constraint fk_likes_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipes_owner_id on recipes (owner_id);
alter table recipes add constraint fk_recipes_owner_id foreign key (owner_id) references users (id) on delete restrict on update restrict;

create index ix_recipes_steps_recipes_id on recipes_steps (recipes_id);
alter table recipes_steps add constraint fk_recipes_steps_recipes_id foreign key (recipes_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipe_ingredients_recipe_id on recipe_ingredients (recipe_id);
alter table recipe_ingredients add constraint fk_recipe_ingredients_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipe_ingredients_ingredient_id on recipe_ingredients (ingredient_id);
alter table recipe_ingredients add constraint fk_recipe_ingredients_ingredient_id foreign key (ingredient_id) references ingredients (id) on delete restrict on update restrict;

alter table users add constraint fk_users_dietary_restrictions_id foreign key (dietary_restrictions_id) references dietary_restrictions (id) on delete restrict on update restrict;

