-- ENUMS
CREATE TYPE user_type AS enum ('BARBER', 'CUSTOMER');

-- TABLES

CREATE TABLE app_user
(
    id        uuid,
    name      varchar(255) NOT NULL,
    phone     varchar(20),
    email     varchar(255) NOT NULL,
    password  varchar(255) NOT NULL,
    user_type user_type    NOT NULL,
    CONSTRAINT pk_app_user_id PRIMARY KEY (id)
);


CREATE TABLE barber_availability
(
    id          uuid PRIMARY KEY,
    barber_id   uuid        NOT NULL REFERENCES app_user (id),
    day_of_week varchar(20) NOT NULL CHECK ( day_of_week IN
                                             ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY')),
    start_time  time        NOT NULL,
    end_time    time        NOT NULL
);

CREATE TABLE appointment
(
    id          uuid                                                                           NOT NULL,
    appt_day    date                                                                           NOT NULL,
    start_time  time                                                                           NOT NULL,
    end_time    time                                                                           NOT NULL,
    customer_id uuid                                                                           NOT NULL,
    barber_id   uuid,
    status      varchar(20) check ( status in ('REQUESTED', 'ACCEPTED', 'DENIED', 'CANCELED')) NOT NULL DEFAULT 'REQUESTED',
    service     varchar(20) check (service in ('HAIRCUT', 'BEARD'))                            NOT NULL,
    CONSTRAINT pk_appointment_id PRIMARY KEY (id),
    CONSTRAINT fk_appointment_customer FOREIGN KEY (customer_id) REFERENCES app_user (id),
    CONSTRAINT fk_appointment_barber FOREIGN KEY (barber_id) REFERENCES app_user (id),
    CONSTRAINT unique_appointment UNIQUE (appt_day, start_time, end_time, customer_id)
);
