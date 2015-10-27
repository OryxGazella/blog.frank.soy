CREATE TABLE posts
(
    id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR(255),
    body VARCHAR,
    rendered_body VARCHAR,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    abstract VARCHAR,
    rendered_abstract VARCHAR
);
