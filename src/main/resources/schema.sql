drop table if exists todo cascade;
create table todo (id uuid not null, content BLOB, created_on timestamp(6), owner varchar(255), primary key (id));
