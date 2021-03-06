create sequence hibernate_sequence start with 1 increment by 1
create table addresses (id bigint generated by default as identity, address_id varchar(255), city varchar(255), country varchar(255), street_name varchar(255), zip_code varchar(255), users_id bigint, primary key (id))
create table author_book (books_id bigint not null, authors_id bigint not null, primary key (books_id, authors_id))
create table authors (id bigint generated by default as identity, first_name varchar(255), last_name varchar(255), primary key (id))
create table books (id bigint generated by default as identity, isbn varchar(255), title varchar(255), primary key (id))
create table carts (id bigint generated by default as identity, cart_id varchar(255), user_id bigint, primary key (id))
create table carts_books (carts_id bigint not null, books_id bigint not null)
create table mtm_order2book (orders_id bigint not null, books_id bigint not null, primary key (orders_id, books_id))
create table orders (id bigint generated by default as identity, order_id varchar(255), user_id bigint, primary key (id))
create table users (id bigint not null, email varchar(255), email_verification_status boolean, encrypted_password varchar(255), first_name varchar(255), last_name varchar(255), role varchar(255), user_id varchar(255), cart_id bigint, email_verification_token_id bigint, primary key (id))
create table verification_tokens (id bigint generated by default as identity, expiration_date timestamp, token varchar(255), user_id bigint, primary key (id))
alter table carts_books add constraint UK_k0b1ni97k18ujy1skwq70kwyb unique (books_id)
alter table addresses add constraint FKr9ce5hb61hmpvm107ccip0irl foreign key (users_id) references users
alter table author_book add constraint FKrdj6b7hxau3ro30gstjnjhsq2 foreign key (authors_id) references authors
alter table author_book add constraint FKdmhd6tpkpwgm2mrjf3735xrwv foreign key (books_id) references books
alter table carts add constraint FKb5o626f86h46m4s7ms6ginnop foreign key (user_id) references users
alter table carts_books add constraint FK8xtdn5r4f1a89vd45ii9bo80a foreign key (books_id) references books
alter table carts_books add constraint FKaf4lswsdrpsk87pj68e2lg06v foreign key (carts_id) references carts
alter table mtm_order2book add constraint FK6tje04avi9ts2t57cfvlk70mp foreign key (books_id) references books
alter table mtm_order2book add constraint FKa77ppli6f51vg2m136kglhx5x foreign key (orders_id) references orders
alter table orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users
alter table users add constraint FKdv26y3bb4vdmsr89c9ppnx85w foreign key (cart_id) references carts
alter table users add constraint FKc2ohfve6xedbgraltt572hcfx foreign key (email_verification_token_id) references verification_tokens
alter table verification_tokens add constraint FK54y8mqsnq1rtyf581sfmrbp4f foreign key (user_id) references users
