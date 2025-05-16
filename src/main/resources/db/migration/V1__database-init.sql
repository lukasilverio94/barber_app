-- ENUMS
CREATE TYPE user_type AS enum ('BARBER', 'CUSTOMER');
CREATE TYPE appointment_status AS enum ('REQUESTED', 'CONFIRMED', 'DENIED', 'CANCELED');
CREATE TYPE service_type AS enum ('HAIRCUT', 'BEARD', 'HAIRCUT_AND_BEARD');

-- TABLES

CREATE TABLE app_user
(
    id        uuid,
    name      varchar(255) NOT NULL,
    phone     varchar(20),
    email     varchar(255) NOT NULL,
    user_type user_type    NOT NULL,
    CONSTRAINT pk_app_user_id PRIMARY KEY (id)
);

-- Separadamente tu cria uma tabela com os horários disponíveis
-- pra um barbeador, ai nela tu referencia o id do barbeador que tá com aquela vaga
-- Nessa tabla tu só colocaria os que tão disponpiveis. Assim que não tiver mais disponivel tu precisa remover
-- ou criar um outro status tipo "OCUPADO"
CREATE TABLE timeslot
(
    id                    uuid                                                                       NOT NULL,
    day                   date                                                                       NOT NULL,
    start_time            time                                                                       NOT NULL,
    end_time              time                                                                       NOT NULL,
    barber_id             uuid                                                                       NOT NULL,
    timeslot_availability varchar(20) check ( timeslot_availability in ('AVAILABLE', 'UNAVAILABLE')) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT pk_timeslot_id PRIMARY KEY (id),
    CONSTRAINT fk_timeslot_barber FOREIGN KEY (barber_id) REFERENCES app_user (id),
    CONSTRAINT unique_timeslot UNIQUE (day, start_time, end_time, barber_id)
);

-- Esse aqui seria a tabela com as consultas que já estão marcadas ou então
-- esperando o barbeiro confirmar ou negar e tal
-- na tua aplicação tu tem que fazer a regra de negócio de lidar com os dados dessas duas tabelas
-- a de horários disponíveis e a de consultas que já existem.
CREATE TABLE appointment
(
    id          uuid               NOT NULL,
    day         date               NOT NULL,
    start_time  time               NOT NULL,
    end_time    time               NOT NULL,
    customer_id uuid               NOT NULL,
    timeslot_id uuid               NOT NULL,
    status      appointment_status NOT NULL DEFAULT 'REQUESTED',
    service     service_type       NOT NULL,
    CONSTRAINT pk_appointment_id PRIMARY KEY (id),
    CONSTRAINT fk_appointment_customer FOREIGN KEY (customer_id) REFERENCES app_user (id),
    CONSTRAINT fk_appointment_timeslot_id FOREIGN KEY (timeslot_id) REFERENCES timeslot (id),
    CONSTRAINT unique_appointment UNIQUE (day, start_time, end_time, customer_id, timeslot_id)
);

