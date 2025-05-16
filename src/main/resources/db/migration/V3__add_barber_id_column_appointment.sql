ALTER TABLE appointment ADD COLUMN barber_id uuid;

ALTER TABLE appointment
ADD CONSTRAINT fk_appointment_barber
FOREIGN KEY (barber_id) REFERENCES app_user (id);