CREATE TABLE IF NOT exists allocation_types (
	id UUID NOT NULL DEFAULT gen_random_uuid(),
	name VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);
