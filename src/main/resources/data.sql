-- DELETE FROM user;

insert into user_table (id, email, is_blocked, name, password, surname, username, role) values
    (1, 'example@example.com', false, 'John', '$2a$10$8IoXLJvZF7CLX1pkH0XQoeF81jHracfkxo9p/jLsXfMmEdKlGVa6W', 'Doe', 'user', 'USER');

-- Restarting sequences for H2 database
ALTER TABLE user_table ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM user_table);
