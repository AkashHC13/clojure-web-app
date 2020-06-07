CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email varchar(255) NOT NULL UNIQUE,
  phone  varchar(255) NOT NULL,
  password varchar(255),
  courses varchar,
  timestamp timestamp DEFAULT current_timestamp
);
