-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO users(username,password,enabled) VALUES ('organizador1','0rg4n1z4d0r',TRUE);
INSERT INTO authorities VALUES ('organizador1','organizador');

INSERT INTO users(username,password,enabled) VALUES ('organizador2','0rg4n1z4d0r',TRUE);
INSERT INTO authorities VALUES ('organizador2','organizador');

INSERT INTO organizadores VALUES (1, 'Fernández', 'Juan', 'Prueba1', 'Prueba1', 'organizador1');
INSERT INTO organizadores VALUES (2,  'Limón', 'Maria', 'Prueba2', 'Prueba2', 'organizador2');
	
	
-- Managers

INSERT INTO users(username,password,enabled) VALUES ('manager1','manager1',TRUE);
INSERT INTO authorities VALUES ('manager1','manager');

INSERT INTO users(username,password,enabled) VALUES ('manager2','manager2',TRUE);
INSERT INTO authorities VALUES ('manager2','manager');

INSERT INTO managers VALUES (3, 'Rodríguez', 'Rosa', 'Prueba1', 'Prueba1', 'manager1');
INSERT INTO managers VALUES (4, 'Domínguez', 'Carlos', 'Prueba2', 'Prueba2', 'manager2');

-- Residencias

INSERT INTO residencias VALUES (1, false, 100, 'residencia1@mail.es', 'Descripcion de prueba', 'Direccion', '70', '07:00', '21:00', 
	'http://www.resi1.com', 'Residencia 1', '987654321', 3);
  
INSERT INTO residencias VALUES (2, false, 100, 'residencia2@mail.es', 'Descripcion de prueba 2', 'Direccion 2', '70', '07:00', '21:00', 
	'http://www.resi2.com', 'Residencia 2', '987654321', 4);
  
-- Acciones
INSERT INTO buenas_acciones VALUES (1, 'descrip', '2020-10-10', 1);
	
INSERT INTO buenas_acciones VALUES (2, 'descrip', '2020-10-10', 1);

INSERT INTO buenas_acciones VALUES (3, 'descrip', '2020-10-10', 1);

INSERT INTO buenas_acciones VALUES (4, 'descrip', '2020-10-10', 2); 

INSERT INTO incidencias VALUES (1, 'descrip', '2020-10-10', 1);

INSERT INTO incidencias VALUES (2, 'descrip', '2020-10-10', 2);



-- Actividades

INSERT INTO actividades VALUES (1, 'Descripcion de prueba', '2010-09-07',  '22:30', '17:00', 
	'Prueba', 1);	
-- Ancianos

INSERT INTO users(username,password,enabled) VALUES ('anciano1','anciano1',TRUE);
INSERT INTO authorities VALUES ('anciano1','anciano');

INSERT INTO users(username,password,enabled) VALUES ('anciano2','anciano2',TRUE);
INSERT INTO authorities VALUES ('anciano2','anciano');

INSERT INTO ancianos VALUES (5, 'García', 'Anacleto', 'Presentacion1', 70, false, 'anciano1');
INSERT INTO ancianos VALUES (6, 'Prieto', 'Dolores', 'Presentacion2', 80, true, 'anciano2');

-- Incidencias

INSERT INTO incidencias VALUES (1, 'Descripcion de incidencia', '2010-09-07', 'titulo1', 1);

-- Buenas acciones

INSERT INTO buenas_acciones VALUES (1, 'Descripcion de buena acción', '2010-09-07', 'titulo1', 1);

-- Inscripciones

INSERT INTO inscripciones VALUES (1, 'Declaración1', 'pendiente', '2020-02-01', null , 5, 1);
INSERT INTO inscripciones VALUES (2, 'Declaración2', 'pendiente', '2020-02-01', null , 6, 1);

-- Excursiones

INSERT INTO excursiones VALUES (1, 'Descripcion de prueba', '2020-01-01',  '22:30', '17:00', 
	'Prueba', '2020-09-07', TRUE, '100', '2.0', 1);
INSERT INTO excursiones VALUES (2, 'Descripcion de prueba2', '2020-09-07',  '22:30', '17:00', 
	'Prueba2', '2020-09-07', TRUE, '100', '0.0', 1);


INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');
