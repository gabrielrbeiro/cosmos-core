CREATE SEQUENCE IF NOT EXISTS public.snowflake_ref;
CREATE OR REPLACE FUNCTION public.id_generator()
    RETURNS BIGINT
    LANGUAGE 'plpgsql'
AS
$BODY$
DECLARE
    our_epoch  BIGINT := 1577836800000;
    seq_id     BIGINT;
    now_millis BIGINT;
    shard_id   INT    := 1;
    result     BIGINT := 0;
BEGIN
    SELECT nextval('public.snowflake_ref') % 1024 INTO seq_id;

    SELECT FLOOR(EXTRACT(EPOCH FROM clock_timestamp()) * 1000) INTO now_millis;
    result := (now_millis - our_epoch) << 23;
    result := result | (shard_id << 10);
    result := result | (seq_id);
    return result;
END;
$BODY$;

CREATE TABLE IF NOT EXISTS user_account
(
    id            BIGINT                   NOT NULL,
    first_name    VARCHAR(180)             NOT NULL,
    last_name     VARCHAR(180),
    username      VARCHAR(60)              NOT NULL,
    email_address VARCHAR(180)             NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_update   TIMESTAMP WITH TIME ZONE,
    enabled       BOOLEAN                  NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_credential
(
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user_credential FOREIGN KEY (user_id) REFERENCES user_account (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    context TEXT   NOT NULL,
    hash    TEXT   NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS access_group
(
    id          BIGINT                   NOT NULL,
    name        VARCHAR(120)             NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_update TIMESTAMP WITH TIME ZONE,
    enabled     BOOLEAN                  NOT NULL DEFAULT true,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS user_group_attachment
(
    user_id  BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    CONSTRAINT fk_ug_attach_user FOREIGN KEY (user_id) REFERENCES user_account (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ug_attach_group FOREIGN KEY (group_id) REFERENCES access_group (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    primary key (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS auth_method
(
    id          BIGINT                   NOT NULL,
    method      VARCHAR(32)              NOT NULL,
    name        VARCHAR(120)             NOT NULL,
    precedence  INT                      NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_update TIMESTAMP WITH TIME ZONE,
    enabled     BOOLEAN                  NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS auth_method_option
(
    id        BIGINT      NOT NULL,
    method_id BIGINT      NOT NULL,
    CONSTRAINT fk_auth_method_option FOREIGN KEY (method_id) REFERENCES auth_method (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    name      VARCHAR(48) NOT NULL,
    value     VARCHAR(120),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_binding
(
    id          BIGINT NOT NULL,
    method_id   BIGINT NOT NULL,
    CONSTRAINT fk_user_binding_auth FOREIGN KEY (method_id) REFERENCES auth_method (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    binding_ref VARCHAR(220),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS system_setting
(
    name  VARCHAR(80) NOT NULL,
    value VARCHAR(180),
    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS action
(
    code        VARCHAR(60) NOT NULL,
    description TEXT,
    PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS group_grant
(
    action_code VARCHAR(60) NOT NULL,
    group_id    BIGINT      NOT NULL,
    CONSTRAINT fk_grant_action FOREIGN KEY (action_code) REFERENCES action (code)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_grant_group FOREIGN KEY (group_id) REFERENCES access_group (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (action_code, group_id)
);
