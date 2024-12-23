-- функция по тестовому заполнению таблиц
CREATE OR REPLACE FUNCTION fill_tables()
RETURNS VOID AS $$

DECLARE
    i INT;
    j INT;
    bookid INT;
	genreid INT;

BEGIN
        -- Добавление жанров
    FOR i IN 1..100 LOOP
            INSERT INTO genres(name) VALUES ('Genre ' || i);
    END LOOP;

        -- Добавление авторов
    FOR i IN 1..1000 LOOP
            INSERT INTO authors(full_name) VALUES ('Author ' || i);
    END LOOP;

            -- Добавление книг
    FOR i IN 1..1000000 LOOP
            -- Вставка книги
            INSERT INTO books(title, author_id)
            VALUES ('Book ' || i, FLOOR(RANDOM() * 100) + 1);
    END LOOP;

        -- Добавление книг-жанров
    FOR i IN 1..10000 LOOP  -- Число вставляемых записей, при необходимости измените на нужное
            bookid := FLOOR(RANDOM() * 1000001) + 1;  -- book_id от 1 до 1000000
            genreid := FLOOR(RANDOM() * 100) + 1;  -- genre_id от 1 до 100

    INSERT INTO books_genres(book_id, genre_id)
    VALUES (bookid, genreid);
    END LOOP;

        -- Добавление комментариев
    FOR i IN 1..100000 LOOP
            bookid := FLOOR(RANDOM() * 1000001) + 1;  -- book_id от 1 до 1000000
    INSERT INTO comments(book_id, text)
    VALUES (bookid, 'Comment text for book ' || bookid);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- запуск выполнения функции заполнения таблиц тестовыми данными
SELECT fill_tables();