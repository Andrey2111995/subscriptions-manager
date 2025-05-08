CREATE TABLE IF NOT EXISTS users
(
  id UUID PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  phone_number VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions
(
  id UUID PRIMARY KEY,
  status VARCHAR(10) NOT NULL,
  service_name VARCHAR(50) NOT NULL,
  user_id UUID REFERENCES users(id),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);