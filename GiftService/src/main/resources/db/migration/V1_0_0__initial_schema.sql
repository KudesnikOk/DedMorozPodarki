CREATE TABLE IF NOT EXISTS gifts (
    id identity NOT NULL PRIMARY KEY,
    quantity int,
    kind varchar (50),
    CONSTRAINT UC_Kind UNIQUE (kind)
);

CREATE TABLE IF NOT EXISTS childs (
    id identity NOT NULL PRIMARY KEY,
    first_name varchar (50),
    second_name varchar (50),
    middle_name varchar (50)
);

CREATE TABLE IF NOT EXISTS deliveries (
    id identity NOT NULL PRIMARY KEY,
    child_id bigint NOT NULL,
    gift_id bigint NOT NULL,
    FOREIGN KEY (child_id) REFERENCES childs(id),
    FOREIGN KEY (gift_id) REFERENCES gifts(id)
);
