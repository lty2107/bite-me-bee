CREATE TABLE IF NOT EXISTS roles (
	id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"name" varchar(20) NULL,
	CONSTRAINT pk_roles_id PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS roles
    OWNER to bitemebee;

INSERT INTO roles (id, "name") VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');