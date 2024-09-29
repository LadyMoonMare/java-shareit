DROP TABLE IF EXISTS app_users, requests, items, bookings, comments;

CREATE TABLE IF NOT EXISTS app_users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 description VARCHAR(2000) NOT NULL,
 requestor_id BIGINT NOT NULL,
 CONSTRAINT pk_request PRIMARY KEY (id),
 FOREIGN KEY (requestor_id) REFERENCES app_users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 name VARCHAR(255) NOT NULL,
 description VARCHAR(2000) NOT NULL,
 is_available BOOLEAN NOT NULL,
 owner_id BIGINT NOT NULL,
 request_id BIGINT,
 CONSTRAINT pk_item PRIMARY KEY (id),
 FOREIGN KEY (owner_id) REFERENCES app_users(id) ON DELETE CASCADE,
 FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS bookings (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 booker_id BIGINT NOT NULL,
 status VARCHAR(50) NOT NULL,
 CONSTRAINT pk_booking PRIMARY KEY (id),
 FOREIGN KEY (booker_id) REFERENCES app_users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 text VARCHAR(2000) NOT NULL,
 item_id BIGINT NOT NULL,
 author_id BIGINT NOT NULL,
 CONSTRAINT pk_comment PRIMARY KEY (id),
 FOREIGN KEY (author_id) REFERENCES app_users(id) ON DELETE CASCADE,
 FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);
