CREATE TYPE user_type AS ENUM ('BARBER', 'CUSTOMER');

CREATE TYPE day_of_week_enum AS ENUM (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'
);

CREATE TYPE appointment_status AS ENUM (
    'REQUESTED', 'ACCEPTED', 'DENIED', 'CANCELED'
);

CREATE TYPE service_type AS ENUM (
    'HAIRCUT', 'BEARD'
);

CREATE TABLE app_user
(
    id        uuid         NOT NULL,
    name      varchar(255) NOT NULL,
    phone     varchar(20),
    email     varchar(255) NOT NULL,
    password  varchar(255) NOT NULL,
    user_type user_type    NOT NULL,
    CONSTRAINT pk_app_user_id PRIMARY KEY (id)
);


CREATE TABLE barber_availability
(
    id          uuid             NOT NULL,
    barber_id   uuid             NOT NULL,
    day_of_week day_of_week_enum NOT NULL,
    start_time  time             NOT NULL,
    end_time    time             NOT NULL,

    CONSTRAINT pk_barber_availability
        PRIMARY KEY (id),

    CONSTRAINT fk_barber_availability_barber
        FOREIGN KEY (barber_id)
            REFERENCES app_user (id),

    CONSTRAINT chk_barber_availability_time
        CHECK (start_time < end_time)
);

CREATE TABLE appointment
(
    id          uuid               NOT NULL,
    appt_day    date               NOT NULL,
    start_time  time               NOT NULL,
    end_time    time               NOT NULL,
    customer_id uuid               NOT NULL,
    barber_id   uuid,
    status      appointment_status NOT NULL DEFAULT 'REQUESTED',
    service     service_type       NOT NULL,

    CONSTRAINT pk_appointment_id
        PRIMARY KEY (id),

    CONSTRAINT fk_appointment_customer
        FOREIGN KEY (customer_id)
            REFERENCES app_user (id),

    CONSTRAINT fk_appointment_barber
        FOREIGN KEY (barber_id)
            REFERENCES app_user (id),

    CONSTRAINT unique_appointment
        UNIQUE (appt_day, start_time, end_time, customer_id),

    CONSTRAINT chk_appointment_time
        CHECK (start_time < end_time)
);
