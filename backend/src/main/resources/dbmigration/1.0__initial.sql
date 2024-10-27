-- apply changes
create table circle (
  id                            bigint generated by default as identity not null,
  is_public                     boolean default false not null,
  title                         varchar(255) not null,
  constraint pk_circle primary key (id)
);

create table circle_recipes (
  circle_id                     bigint not null,
  recipes_id                    bigint not null,
  constraint pk_circle_recipes primary key (circle_id,recipes_id)
);

create table circle_circle_users (
  circle_id                     bigint not null,
  circle_users_id               bigint not null,
  constraint pk_circle_circle_users primary key (circle_id,circle_users_id)
);

create table circle_users (
  id                            bigint generated by default as identity not null,
  user_id                       bigint,
  circle_id                     bigint,
  role                          integer not null,
  pending                       boolean default false not null,
  constraint ck_circle_users_role check ( role in (0,1,2)),
  constraint pk_circle_users primary key (id)
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

create table followers (
  id                            bigint generated by default as identity not null,
  follower_id                   bigint not null,
  user_id                       bigint not null,
  pending                       boolean default false not null,
  followed_since                timestamp not null,
  constraint pk_followers primary key (id)
);

create table ingredients (
  id                            bigint generated by default as identity not null,
  recipe_id                     bigint not null,
  type                          integer not null,
  name                          varchar(255) not null,
  constraint ck_ingredients_type check ( type in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_ingredients primary key (id)
);

create table likes (
  id                            bigint generated by default as identity not null,
  user_id                       bigint not null,
  recipe_id                     bigint not null,
  created_ad                    timestamp not null,
  constraint pk_likes primary key (id)
);

create table recipes (
  id                            bigint generated by default as identity not null,
  local_id                      bigint not null,
  creation_date                 bigint not null,
  modification_date             bigint not null,
  owner_id                      bigint,
  title                         varchar(255) not null,
  description                   varchar(255) not null,
  constraint pk_recipes primary key (id)
);

create table recipes_steps (
  recipes_id                    bigint not null,
  value                         varchar(255) not null
);

create table recipes_circle (
  recipes_id                    bigint not null,
  circle_id                     bigint not null,
  constraint pk_recipes_circle primary key (recipes_id,circle_id)
);

create table recipe_ingredients (
  id                            bigint generated by default as identity not null,
  recipe_id                     bigint,
  ingredient_id                 bigint,
  amount                        float not null,
  unit                          integer not null,
  constraint ck_recipe_ingredients_unit check ( unit in (0,1,2,3,4,5,6)),
  constraint pk_recipe_ingredients primary key (id)
);

create table users (
  id                            bigint generated by default as identity not null,
  role                          integer not null,
  diet_id                       bigint not null,
  username                      varchar(255) not null,
  constraint ck_users_role check ( role in (0,1)),
  constraint uq_users_diet_id unique (diet_id),
  constraint pk_users primary key (id)
);

create table users_circle (
  users_id                      bigint not null,
  circle_id                     bigint not null,
  constraint pk_users_circle primary key (users_id,circle_id)
);

-- foreign keys and indices
create index ix_circle_recipes_circle on circle_recipes (circle_id);
alter table circle_recipes add constraint fk_circle_recipes_circle foreign key (circle_id) references circle (id) on delete restrict on update restrict;

create index ix_circle_recipes_recipes on circle_recipes (recipes_id);
alter table circle_recipes add constraint fk_circle_recipes_recipes foreign key (recipes_id) references recipes (id) on delete restrict on update restrict;

create index ix_circle_circle_users_circle on circle_circle_users (circle_id);
alter table circle_circle_users add constraint fk_circle_circle_users_circle foreign key (circle_id) references circle (id) on delete restrict on update restrict;

create index ix_circle_circle_users_circle_users on circle_circle_users (circle_users_id);
alter table circle_circle_users add constraint fk_circle_circle_users_circle_users foreign key (circle_users_id) references circle_users (id) on delete restrict on update restrict;

create index ix_circle_users_user_id on circle_users (user_id);
alter table circle_users add constraint fk_circle_users_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_circle_users_circle_id on circle_users (circle_id);
alter table circle_users add constraint fk_circle_users_circle_id foreign key (circle_id) references circle (id) on delete restrict on update restrict;

create index ix_followers_follower_id on followers (follower_id);
alter table followers add constraint fk_followers_follower_id foreign key (follower_id) references users (id) on delete restrict on update restrict;

create index ix_followers_user_id on followers (user_id);
alter table followers add constraint fk_followers_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_ingredients_recipe_id on ingredients (recipe_id);
alter table ingredients add constraint fk_ingredients_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_likes_user_id on likes (user_id);
alter table likes add constraint fk_likes_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_likes_recipe_id on likes (recipe_id);
alter table likes add constraint fk_likes_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipes_owner_id on recipes (owner_id);
alter table recipes add constraint fk_recipes_owner_id foreign key (owner_id) references users (id) on delete restrict on update restrict;

create index ix_recipes_steps_recipes_id on recipes_steps (recipes_id);
alter table recipes_steps add constraint fk_recipes_steps_recipes_id foreign key (recipes_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipes_circle_recipes on recipes_circle (recipes_id);
alter table recipes_circle add constraint fk_recipes_circle_recipes foreign key (recipes_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipes_circle_circle on recipes_circle (circle_id);
alter table recipes_circle add constraint fk_recipes_circle_circle foreign key (circle_id) references circle (id) on delete restrict on update restrict;

create index ix_recipe_ingredients_recipe_id on recipe_ingredients (recipe_id);
alter table recipe_ingredients add constraint fk_recipe_ingredients_recipe_id foreign key (recipe_id) references recipes (id) on delete restrict on update restrict;

create index ix_recipe_ingredients_ingredient_id on recipe_ingredients (ingredient_id);
alter table recipe_ingredients add constraint fk_recipe_ingredients_ingredient_id foreign key (ingredient_id) references ingredients (id) on delete restrict on update restrict;

alter table users add constraint fk_users_diet_id foreign key (diet_id) references dietary_restrictions (id) on delete restrict on update restrict;

create index ix_users_circle_users on users_circle (users_id);
alter table users_circle add constraint fk_users_circle_users foreign key (users_id) references users (id) on delete restrict on update restrict;

create index ix_users_circle_circle on users_circle (circle_id);
alter table users_circle add constraint fk_users_circle_circle foreign key (circle_id) references circle (id) on delete restrict on update restrict;

