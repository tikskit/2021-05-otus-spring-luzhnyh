insert into authors(surname, name) values('Васильев', 'Владимир'), ('Лукьяненко', 'Сергей');
insert into genres(name) values('sci-fi'), ('fantasy');

insert into users(login, pass, role)
    values('Manager', '$2a$10$uJCGK5f8/ZeEbLsStaP9..qhlR/VF9GHQXw247CHh/nwnKzI.A7du', 'BOOK_MANAGER'),
    ('Supporter', '$2a$10$tBgzme5LEv7nfVk6/obmwu/M9xEQHCgfigRx4i4Ux07m2BSmESZh.', 'BOOK_SUPPORTER'),
    ('Reviewer', '$2a$10$0cE/w2eaiwe5amANFSV9UOhkS6.Y8U1ha.qb5mNYjABMeuAYriVsK', 'BOOK_REVIEWER');