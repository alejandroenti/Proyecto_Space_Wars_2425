CREATE DATABASE `SpaceWars`;
/*DROP DATABASE IF EXISTS `SpaceWars`;*/
USE SpaceWars;

-- PLANET STATS
CREATE TABLE IF NOT EXISTS planet_stats(
	planet_id INT PRIMARY KEY AUTO_INCREMENT,
    name_planet VARCHAR(20),
    resource_metal_amount INT,
    resource_deuterium_amount INT,
    technology_defense_level INT,
    technology_attack_level INT,
    battles_counter INT,
    missile_launcher_remaining INT,
    ion_canon_remaining INT,
    plasma_canon_remaining INT,
    light_hunter_remaining INT,
    heavy_hunter_remaining INT,
    battleship_remaining INT,
    armored_ship_remaining INT
);

/* DROP TABLE planet_stats; */ 

-- -------------------------------------------- * --------------------------------------------

-- BATTLE STATS
CREATE TABLE IF NOT EXISTS battle_stats(
	planet_id INT NOT NULL,
    num_battle INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    resource_metal_acquired INT,
    resource_deuterium_acquired INT,
    waste_metal_generated INT,
    waste_deuterium_generated INT,
    FOREIGN KEY (planet_id) REFERENCES planet_stats(planet_id) ON DELETE CASCADE
);

/* DROP TABLE battle_stats; */ 

-- -------------------------------------------- * --------------------------------------------

-- PLANET BATTLE DEFENSE
CREATE TABLE IF NOT EXISTS planet_battle_defense(
	planet_id INT NOT NULL,
	num_battle INT NOT NULL,
    missile_launcher_built INT,
    missile_launcher_destroyed INT,
    ion_cannon_built INT,
	ion_cannon_destroyed INT,
    plasma_canon_built INT,
    plasma_canon_destroyed INT,
    PRIMARY KEY (planet_id, num_battle),
    FOREIGN KEY (planet_id,num_battle) REFERENCES battle_stats(planet_id,num_battle) ON DELETE CASCADE
);

/* DROP TABLE planet_battle_defense; */ 

-- -------------------------------------------- * --------------------------------------------

-- PLANET BATTLE ARMY
CREATE TABLE IF NOT EXISTS planet_battle_army(
	planet_id INT NOT NULL,
	num_battle INT NOT NULL,
    light_hunter_built INT,
    light_hunter_destroyed INT,
    heavy_hunter_built INT,
    heavy_hunter_destroyed INT,
    battleship_built INT,
    battleship_destroyed INT,
    armored_ship_built INT,
    armored_ship_destroyed INT,
    PRIMARY KEY (planet_id, num_battle),
    FOREIGN KEY (planet_id,num_battle) REFERENCES battle_stats(planet_id,num_battle) ON DELETE CASCADE
);

/* DROP TABLE planet_battle_army; */ 

-- -------------------------------------------- * --------------------------------------------

-- ENEMY ARMY
CREATE TABLE  IF NOT EXISTS enemy_army(
	planet_id INT NOT NULL,
	num_battle INT NOT NULL,
    light_hunter_threat INT,
    light_hunter_destroyed INT,
    heavy_hunter_threat INT,
    heavy_hunter_destroyed INT,
    battleship_threat INT,
    battleship_destroyed INT,
    armored_ship_threat INT,
    armored_ship_destroyed INT,
	PRIMARY KEY (planet_id, num_battle),
    FOREIGN KEY (planet_id,num_battle) REFERENCES battle_stats(planet_id,num_battle) ON DELETE CASCADE
);

/* DROP TABLE enemy_army; */ 

-- -------------------------------------------- * --------------------------------------------

-- BATTLE LOG
CREATE TABLE IF NOT EXISTS battle_log(
	planet_id INT NOT NULL,
	num_battle INT NOT NULL,
    num_line INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    log_entry VARCHAR(500),
	FOREIGN KEY (planet_id,num_battle) REFERENCES battle_stats(planet_id,num_battle) ON DELETE CASCADE
);

/* DROP TABLE enemy_army; */ 

-- -------------------------------------------- INSERTS DE PRUEBA --------------------------------------------

-- PLANET_STATS
INSERT INTO planet_stats (name_planet, resource_metal_amount, resource_deuterium_amount, technology_defense_level, technology_attack_level, battles_counter, missile_launcher_remaining, ion_canon_remaining, plasma_canon_remaining, light_hunter_remaining, heavy_hunter_remaining, battleship_remaining, armored_ship_remaining)
VALUES
('Terra', 50000, 20000, 3, 5, 12, 100, 50, 30, 200, 150, 80, 60),
('Mars', 30000, 15000, 2, 4, 10, 120, 40, 25, 180, 130, 70, 50),
('Venus', 60000, 25000, 4, 6, 15, 80, 60, 40, 250, 180, 90, 70),
('Jupiter', 80000, 30000, 5, 7, 20, 90, 70, 50, 300, 200, 100, 80),
('Saturn', 70000, 22000, 3, 6, 18, 110, 55, 35, 220, 170, 85, 65);

SELECT * FROM planet_stats;


-- BATTLE_STATS
INSERT INTO battle_stats (planet_id, resource_metal_acquired, resource_deuterium_acquired, waste_metal_generated, waste_deuterium_generated  )
VALUES
(1, 5000, 2000, 300,400),
(2, 3000, 1500, 400,500),
(3, 7000, 2500, 360,600),
(4, 9000, 3000, 700,900),
(5, 8000, 2200, 200,500);
-- VER FUNCIONAMIENTO CLAVES FORANEAS EN JAVA 

SELECT * FROM battle_stats;

-- PLANET_BATTLE_DEFENSE
INSERT INTO planet_battle_defense (
    planet_id, num_battle,
    missile_launcher_built, missile_launcher_destroyed,
    ion_cannon_built, ion_cannon_destroyed,
    plasma_canon_built, plasma_canon_destroyed
) VALUES
(1, 1, 10, 3, 5, 1, 2, 0),
(2, 2, 12, 4, 3, 2, 1, 1),
(3, 3, 8, 2, 4, 0, 3, 1),
(4, 4, 15, 5, 6, 3, 5, 2),
(5, 5, 9, 1, 2, 1, 0, 0);

SELECT * FROM planet_battle_defense;

-- PLANET BATTLE ARMY
INSERT INTO planet_battle_army (
    planet_id, num_battle,
    light_hunter_built, light_hunter_destroyed,
    heavy_hunter_built, heavy_hunter_destroyed,
    battleship_built, battleship_destroyed,
    armored_ship_built, armored_ship_destroyed
) VALUES
(1, 1, 50, 10, 30, 5, 20, 2, 15, 1),
(2, 2, 60, 15, 35, 8, 25, 3, 18, 2),
(3, 3, 70, 20, 40, 10, 30, 5, 22, 3),
(4, 4, 80, 25, 45, 12, 35, 6, 25, 4),
(5, 5, 55, 12, 32, 6, 28, 3, 19, 2);

SELECT * FROM planet_battle_army;

-- ENEMY ARMY
INSERT INTO enemy_army (
    planet_id, num_battle,
    light_hunter_threat, light_hunter_destroyed,
    heavy_hunter_threat, heavy_hunter_destroyed,
    battleship_threat, battleship_destroyed,
    armored_ship_threat, armored_ship_destroyed
) VALUES
(1, 1, 60, 45, 35, 20, 15, 10, 10, 5),
(2, 2, 70, 50, 40, 25, 20, 12, 12, 6),
(3, 3, 80, 60, 45, 30, 25, 18, 14, 8),
(4, 4, 90, 65, 50, 35, 30, 20, 16, 10),
(5, 5, 65, 48, 38, 22, 18, 11, 11, 7);

SELECT * FROM enemy_army;
-- BATTLE LOG
INSERT INTO battle_log (planet_id, num_battle, log_entry) VALUES
(1, 1, 'Iniciada batalla en Terra. Se desplegaron cazas ligeros y cañones de plasma. Enemigo respondido con fuerza moderada.'),
(2, 2, 'Combate en Mars. Se destruyeron la mayoría de los cazas enemigos. Pérdidas mínimas en defensas terrestres.'),
(3, 3, 'Ataque intenso en Venus. Alta amenaza de naves blindadas enemigas. Se logró repeler tras fuertes bajas.'),
(4, 4, 'Jupiter resistió una embestida con gran número de acorazados enemigos. Defensa con pérdidas significativas.'),
(5, 5, 'Saturn interceptó la flota enemiga antes de su aproximación. Se logró una victoria contundente con pocas bajas.');

SELECT * FROM battle_log;







