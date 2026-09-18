drop database if exists CineCol;
create database CineCol;
use CineCol;
 
-- creacion de tablas
create table Director(
id_director int auto_increment primary key,
nombre varchar(50),
apellido varchar(50),
edad int,
fecha_nacimiento datetime,
nacionalidad varchar(50)
);
 
create table Cajero(
id_cajero int auto_increment primary key,
nombre varchar(50),
apellido varchar(50)
);
 
create table Genero(
id_genero int auto_increment primary key, 
nombre_genero varchar(50)
);
 
create table Pelicula(
id_pelicula int auto_increment primary key,
titulo varchar(100),
director varchar(100),
anio_estreno int(10),
duracion varchar(50),
id_genero int not null,
<<<<<<< HEAD
id_director int null,
=======
id_director int not null,
>>>>>>> d53b7f2728955eb71234f86b6015c8bbb8b852ae
foreign key (id_genero) references Genero(id_genero),
foreign key (id_director) references Director(id_director)
);
 
create table Sala(
id_sala int auto_increment primary key,
nombre_sala varchar(50),
capacidad int not null check(capacidad>0)
);
 
create table Funcion(
id_funcion int auto_increment primary key,
id_sala int not null,
id_pelicula int not null,
fecha datetime, 
hora_funcion time,
foreign key (id_sala) references Sala(id_sala),
foreign key (id_pelicula) references Pelicula(id_pelicula)
);
 
create table Venta(
id_venta int auto_increment primary key,
id_cajero int not null,
id_funcion int not null,
sala int not null,
fecha_venta datetime,
foreign key (id_cajero) references Cajero(id_cajero),
foreign key (id_funcion) references Funcion(id_funcion)
);

-- inserts 
insert into Genero(nombre_genero) 
values ('Accion'), ('Animacion'), ('Aventura'), ('Belico'), ('Ciencia ficcion'), ('Comedia'), ('Comedia dramatica'), 
  ('Crimen'), ('Docuficcion'), ('Documental'), ('Drama'), 
  ('Fantasia'), ('Ficcion'), ('Misterio'), ('Musical'), ('Romance'), ('Suspenso'), ('Terror'), ('Western');
<<<<<<< HEAD
 
=======

>>>>>>> d53b7f2728955eb71234f86b6015c8bbb8b852ae
insert into Pelicula(titulo, director, anio_estreno, duracion, id_genero)
values ('9 DE ABRIL', 'Jairo Estrada / Erick Casanova', 2026, '80 min', 10),
('AA965 UN RESCATE IMPOSIBLE', 'Jörg Hiller García', 2026, '71 min', 10),
('Agarrando pueblo', 'Luis Ospina y Carlos Mayolo', 1978, '28 min', 9),
('AL BORDE DEL PUENTE', 'Xavier Markus', 2024, '70 min', 10),
('Alias María', 'José Luis Rugeles', 2015, 'N/D', 11),
('Amor, mujeres y flores', 'Marta Rodríguez y Jorge Silva', 1988, 'N/D', 10),
('ANDARIEGA', 'Raúl Soto', 2025, '94 min', 10),
('Anna', 'Jacques Toulemonde Vidal', 2017, 'N/D', 11),
('AYUNO Y CENIZAS', 'Rodrigo Dimatė', 2026, '100 min', 10),
('Águilas no cazan moscas', 'Sergio Cabrera', 1994, 'N/D', 6),
('BAJO UNA LLUVIA AJENA', 'Marta Hincapié Uribe', 2024, '84 min', 10),
('BIEN INMUEBLE', 'Emanuel Giraldo Betancur', 2026, '90 min', 10),
('Bolivar soy yo', 'Jorge Ali Triana', 2002, 'N/D', 6),
('BRIGITTE, PLANETA B', 'Santiago Posada', 2025, '87 min', 10),
('Canaguaro', 'Dunay Kuzmanich', 1981, 'N/D', 11),
('Carne de tu carne', 'Carlos Mayolo', 1983, 'N/D', 11),
('CERRO, SALSA Y LEYENDAS', 'Jairo Estrada / Erick Casanova', 2026, '78 min', 10),
('Chircales', 'Marta Rodríguez y Jorge Silva', 1972, '42 min', 10),
('Chocó', 'Jhonny Hendrix Hinestroza', 2012, 'N/D', 11),
('CLAVE DE AMOR', 'Luis Fernando Bernal Polo', 2026, '100 min', 13),
('Confesión a Laura', 'Jaime Osorio Gómez', 1990, 'N/D', 11),
('COROZO', 'Simón Elías', 2026, '97 min', 13),
('CURUPIRA', 'Jairo Estrada / Santiago Vargas', 2026, '78 min', 10),
('Cóndores no entierran todos los días', 'Francisco Norden', 1984, 'N/D', 11),
('Del amor y otros demonios', 'Hilda Hidalgo', 2009, 'N/D', 11),
('DICEN QUE TÚ Y YO ESTAMOS LOCOS', 'Juan Mauricio Piñeros', 2026, '77 min', 10),
('Dios los junta y ellos se separan', 'Harold Trompetero', 2006, 'N/D', 6),
('DISRUPTOR, EL RITMO DEL ALMA', 'Jairo Estrada / Stivens Murcia', 2026, '80 min', 10),
('DOS VECES BESTIA', 'Luis Esguerra Cifuentes', 2026, '72 min', 13),
('El abrazo de la serpiente', 'Ciro Guerra', 2015, '125 min', 11),
('EL BUGEO COLORADO', 'Jairo Estrada / Santiago Vargas', 2026, '78 min', 10),
('El drama del 15 de octubre', 'Hermanos Di Doménico', 1915, 'N/D', 10),
('EL GANCHO', 'Sandra Higuita Marin', 2024, '121 min', 13),
('EL JUEGO DE LA VIDA', 'Andrés Ruíz Zuluaga', 2026, '95 min', 10),
('EL LEGADO DE LAS OLAS', 'Javier Linares / Javier Linares Sabater', 2026, '78 min', 10),
('El man es Germán', 'Harold Trompetero', 2016, 'N/D', 6),
('El olvido que seremos', 'Fernando Trueba', 2020, '136 min', 11),
('El paseo', 'Harold Trompetero', 2010, 'N/D', 6),
('EL PERFECTO CULPABLE', 'Henry Rincón', 2026, '74 min', 13),
('EL REBUSQUE', 'Jairo Estrada / Stivens Murcia', 2026, '79 min', 10),
('El rey', 'Antonio Dorado', 2004, 'N/D', 11),
('EL TITÁN', 'Alexander Giraldo.', 2024, '108 min', 10),
('EL VALOR DE LA PALABRA', 'Marta Rodriguez / Fernando Restrepo', 2026, '79 min', 10),
('EL VAQUERO', 'Emma Rozanski', 2024, '93 min', 13),
('EMPAREJA2', 'Ángel Ayllón', 2025, '85 min', 13),
('EN TIERRA', 'Simón Uribe', 2026, '80 min', 10),
('ENTRE 2 AGUAS', 'Carlos Gabriel Vergara Montiel', 2026, '93 min', 13),
('ENTREVISTA LABORAL', 'Carlos Osuna', 2024, '80 min', 13),
('ESTIMADOS SEÑORES', 'Patricia Castañeda', 2024, '98 min', 13),
('FICCIONES SIN FUTURO', 'Diego Alejandro Espinosa Alzate', 2026, '77 min', 13),
('Gamin', 'Ciro Durán', 1978, 'N/D', 10),
('Gente de bien', 'Franco Lolli', 2014, 'N/D', 11),
('Golpe de estadio', 'Sergio Cabrera', 1998, 'N/D', 6),
('GUATES', 'Diego Gutiérrez', 2024, '72 min', 10),
('HABITANTE', 'José Alejandro González', 2026, '89 min', 10),
('HERENCIA: LOS CANTOS DE LA TIERRA', 'Iván Acosta Rojas', 2026, 'N/D', 10),
('Ilona llega con la lluvia', 'Sergio Cabrera', 1996, 'N/D', 11),
('Impunidad', 'Hollman Morris', 2010, 'N/D', 10),
('INVICTAS', 'Jairo Estrada / Erick Casanova', 2026, '80 min', 10),
('Karen llora en un bus', 'Gabriel Rojas Vera', 2011, 'N/D', 11),
('LA CASA DEL SUR', 'Carina Oroza Daroca / Ramiro Fierro', 2024, '88 min', 13),
('La estrategia del caracol', 'Sergio Cabrera', 1993, '116 min', 7),
('La eterna noche de las doce lunas', 'Priscila Padilla', 2013, 'N/D', 10),
('La jauría', 'Andrés Ramírez Pulido', 2022, 'N/D', 11),
('LA LUZ POR PRIMERA VEZ', 'Frank Rodríguez Rojas / Ibeth Johanna Rey Jerez', 2026, '77 min', 10),
('La mansión de Araucaima', 'Carlos Mayolo', 1986, 'N/D', 11),
('LA MARCHA DEL HAMBRE', 'Sorany Marin Trejos', 2026, '92 min', 10),
('La María', 'Máximo Calvo Olmedo', 1922, 'N/D', 11),
('LA MORTUORIA', 'Fabián Cardona Münera', 2024, '73 min', 13),
('LA PATASOLA', 'Harold Trompetero / Paul Cataño', 2024, '80 min', 13),
('LA PENA MÁXIMA 2', 'Mauricio Cruz Fortunato', 2024, '90 min', 13),
('LA PIEL MÁS TEMIDA', 'Joel Calero', 2024, '121 min', 13),
('La playa D.C.', 'Juan Andrés Arango', 2012, 'N/D', 11),
('LA PRESENCIA DEL VACÍO', 'José Cortés', 2026, '98 min', 13),
('La primera noche', 'Luis Alberto Restrepo', 2003, 'N/D', 11),
('LA RED', 'Juan David Cortés Hernández', 2024, '90 min', 10),
('LA SANGRE DE DIOS', 'Luis Alfredo Velasco Parra', 2026, '82 min', 13),
('La sirga', 'William Vega', 2012, 'N/D', 11),
('La sociedad del semáforo', 'Rubén Mendoza', 2010, 'N/D', 11),
('La sombra del caminante', 'Ciro Guerra', 2004, 'N/D', 11),
('La tierra y la sombra', 'César Acevedo', 2015, '97 min', 11),
('La vendedora de rosas', 'Víctor Gaviria', 1998, '115 min', 11),
('La virgen de los sicarios', 'Barbet Schroeder', 2000, '100 min', 11),
('LACTAR', 'Harold Trompetero', 2026, 'N/D', 13),
('LAS BRAVAS', 'Diana Ojeda', 2026, '91 min', 10),
('LEGAL', 'Daniel Arango', 2026, '77 min', 13),
('LEJOS, AQUÍ', 'Ana Sofía Osorio Ruiz', 2026, '75 min', 13),
('LES DIGO ALMAS', 'Libia Stella Gómez Díaz', 2026, '108 min', 13),
('Litigante', 'Franco Lolli', 2019, 'N/D', 11),
('LLUEVE SOBRE BABEL', 'Gala del Sol', 2026, '113 min', 13),
('Los colores de la montaña', 'Carlos César Arbeláez', 2010, 'N/D', 11),
('Los Reyes del Mundo', 'Laura Mora', 2022, 'N/D', 11),
('Los viajes del viento', 'Ciro Guerra', 2009, '118 min', 11),
('María, llena eres de gracia', 'Joshua Marston', 2004, '101 min', 11),
('Matar a Jesús', 'Laura Mora', 2017, 'N/D', 11),
('Memoria', 'Apichatpong Weerasethakul', 2021, '136 min', 11),
('MI BESTIA', 'Camila Beltrán', 2024, '75 min', 13),
('Mi gente linda, mi gente bella', 'Harold Trompetero', 2018, 'N/D', 6),
('MI PUEBLO INFIERNO, EL INICIO DE UNA GUERRA', 'Victor Alfonso González Ochoa', 2026, '95 min', 13),
('MIRADAS', 'Jairo Estrada', 2026, '83 min', 10),
('Monos', 'Alejandro Landes', 2019, '102 min', 11),
('MOTERAS', 'Jairo Estrada / Erick Casanova', 2026, '77 min', 10),
('NOCHES DE CINE', 'Marco Vélez Esquivia', 2026, '102 min', 13),
('Nuestra voz de tierra, memoria y futuro', 'Marta Rodriguez y Jorge Silva', 1980, 'N/D', 10),
('OJO POR OJO', 'Eduardo Ayllón', 2026, '85 min', 13),
('Oscuro animal', 'Felipe Guerrero', 2016, '100 min', 11),
('Perder es cuestión de método', 'Sergio Cabrera', 2004, 'N/D', 17),
('Perro come perro', 'Carlos Moreno', 2008, 'N/D', 1),
('PIEDRAS PRECIOSAS', 'Simón Vélez', 2026, '70 min', 13),
('Planas: testimonio de un etnocidio', 'Marta Rodriguez', 1971, 'N/D', 10),
('PUKEM SWA (Sol de invierno)', 'Samuel Moreno Álvarez', 2026, '71 min', 10),
('PUNTOS DE FUGA', 'Lina Rodriguez', 2026, '72 min', 10),
('Pura sangre', 'Luis Ospina', 1982, 'N/D', 18),
('PVC-1', 'Spiros Stathoulopoulos', 2007, '85 min', 11),
('Pájaros de verano', 'Ciro Guerra y Cristina Gallego', 2018, '125 min', 11),
('Que viva la música', 'Cristian Díaz Pardo', 2015, 'N/D', 11),
('Roa', 'Andi Baiz', 2013, 'N/D', 11),
('Rodrigo D: No futuro', 'Víctor Gaviria', 1990, '92 min', 11),
('Satanás', 'Andrés Baiz', 2007, 'N/D', 11),
('SEMILLAS DE VIDA', 'Stivens Murcia', 2026, '78 min', 10),
('SOY MÚCURA', 'Nina Marín', 2026, '88 min', 13),
('Soñar no cuesta nada', 'Rodrigo Triana', 2006, 'N/D', 11),
('SUEÑOS EN CONCRETO', 'Viviana Gómez Echeverry / Anton Wenzel', 2024, '91 min', 10),
('Sumas y restas', 'Víctor Gaviria', 2004, 'N/D', 11),
('SUSANA Y ELVIRA: SIN PLAN B', 'María Gamboa Jaramillo', 2026, '113 min', 13),
('TARDE O TEMPRANO', 'Juan Pablo Chajin Cabrera', 2026, '74 min', 13),
('TRES HERMANAS', 'Joyce Ventura', 2026, '80 min', 10),
('TU REINA', 'Luis Alfredo Velasco Parra', 2026, '108 min', 13),
('TUMBADORES', 'Maria Isabel Burnes', 2024, '98 min', 13),
('Técnicas de duelo', 'Sergio Cabrera', 1988, 'N/D', 11),
('UN AMOR INEVITABLE', 'Mauro Mauad', 2026, '86 min', 13),
('UN LUGAR DE LIBERTAD', 'Diego Alejandro Espinosa Alzate', 2026, '76 min', 10),
('Un tigre de papel', 'Luis Ospina', 2007, '100 min', 9),
('UNA HUELLA DE PAZ', 'Fabián Cardona Münera', 2025, '80 min', 13),
('Uno al año no hace daño', 'Juan Camilo Pinzón', 2011, 'N/D', 6),
('VOLAR', 'Jeiver Pinto Vargas', 2025, '79 min', 13),
('Yo soy otro', 'Oscar Campo', 2008, 'N/D', 11),
('YUCHE', 'Jairo Estrada / Santiago Vargas', 2026, '76 min', 10);
 
-- Consulta que usa la aplicacion (los ? los llena Java con PreparedStatement)
-- SELECT p.titulo, p.director, p.anio_estreno, p.duracion, g.nombre_genero
-- FROM Pelicula p
-- LEFT JOIN Genero g ON p.id_genero = g.id_genero
-- WHERE p.titulo LIKE ?
-- ORDER BY p.titulo;
 
-- Verificacion rapida: deben salir 140 peliculas
select count(*) as total_peliculas from Pelicula;
 
select p.titulo, p.director, p.anio_estreno, p.duracion, g.nombre_genero
from Pelicula p
left join Genero g on p.id_genero = g.id_genero
where p.titulo like '%amor%'
order by p.titulo;

