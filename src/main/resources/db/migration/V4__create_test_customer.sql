insert into app_user(id, name, phone, email, user_type)
VALUES (
           gen_random_uuid(),
           'Customer1',
           '+313123131231',
           'test@test.com',
           'CUSTOMER'
       );
