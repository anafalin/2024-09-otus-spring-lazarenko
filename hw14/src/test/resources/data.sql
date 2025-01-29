INSERT INTO authors(full_name)
VALUES ('Author_1'),
       ('Author_2'),
       ('Author_3');

INSERT INTO genres(name)
VALUES ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6');

INSERT INTO books(title, author_id)
VALUES ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

INSERT INTO books_genres(book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

INSERT INTO comments(book_id, text, created_at)
VALUES (1, 'Comment to book_1', '2024-11-17'),
       (2, 'Comment to book_2', '2024-11-17');

INSERT INTO roles(id, name)
VALUES (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'EDITOR');

INSERT INTO users(id, username, firstname, lastname, email, password, is_enabled, is_locked)
VALUES (1, 'admin', 'admin', 'admin', 'admin@mail.ru',
        '$2a$10$Dco01cWfAGLpN1RUetSa2OSGcXQZVGmVx4qnE9IC38tVhzKTy/kpW', true, false),
       (2, 'supervisor', 'supervisor', 'supervisor', 'supervisor@mail.ru',
        '$2a$10$1BDVFM8CjF/MC9CJoPn4C.ZHAJveRxpmnMOXpuv9Yvj0qHfmCivy6', true, false),
       (3, 'user1', 'user1', 'user1', 'user1@mail.ru',
        '$2a$10$WnpbG.mmqzFyVC1g4zZTw.SAb9zcUEfl57eNHrD4LaWV4sF6U4ieK', true, false),
       (4, 'user2', 'user2', 'user2','user2@mail.ru',
        '$2a$10$m4LB5Zw5UOAs0zRArkpUx.GZvToPrUtrJVU7CpXMn3wJ3/s3P5/6y', true, false);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 3),
       (3, 2),
       (4, 2);

/* acl */
INSERT INTO acl_sid (id, principal, sid)
VALUES (1, 1, 'admin'),
       (2, 3, 'supervisor'),
       (3, 2, 'user1'),
       (4, 2, 'user2');

INSERT INTO acl_class (id, class)
VALUES (1, 'ru.otus.hw.dto.BookDto');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 2, 0),
       (2, 1, 2, NULL, 2, 0),
       (3, 1, 3, NULL, 2, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure)
VALUES (1, 1, 1, 1, 2, 1, 1, 1),
       (2, 2, 3, 1, 2, 1, 1, 1),
       (3, 3, 4, 3, 2, 1, 1, 1),
       (4, 3, 5, 2, 2, 1, 1, 1);