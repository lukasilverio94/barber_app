ALTER TABLE app_user
    ALTER COLUMN id SET NOT NULL;

ALTER TABLE app_user
    ADD CONSTRAINT uk_app_user_email UNIQUE(email);

ALTER TABLE app_user
    ADD CONSTRAINT uk_app_user_phone UNIQUE(phone);