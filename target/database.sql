CREATE TABLE staff (
                       id      SERIAL PRIMARY KEY,
                       type    VARCHAR(30)  NOT NULL,
                       name    VARCHAR(100) NOT NULL,
                       address VARCHAR(100) NOT NULL,
                       salary  DOUBLE PRECISION,
                       bonus   DOUBLE PRECISION,
                       hours_worked INT,
                       rate    DOUBLE PRECISION
);

--  Insert sample data from Image 2
INSERT INTO staff (type, name, address, salary, bonus, hours_worked, rate) VALUES
                                                                               ('Volunteer',             'Tina',  'PP',  0.0,   NULL, NULL, NULL),
                                                                               ('SalariedEmployee',      'Dana',  'KPS', 300.0, 10.0, NULL, NULL),
                                                                               ('HourlySalaryEmployee',  'Sokha', 'BTB', NULL,  NULL, 60,   10.0),
                                                                               ('Volunteer',             'Lee',   'SR',  0.0,   NULL, NULL, NULL),
                                                                               ('SalariedEmployee',      'Fy',    'KT',  300.0, 20.0, NULL, NULL),
                                                                               ('HourlySalaryEmployee',  'Ka',    'PV',  NULL,  NULL, 50,   10.0);

select * from staff;