DROP DATABASE IF EXISTS soccorso_web;
CREATE DATABASE soccorso_web;
USE soccorso_web;

CREATE TABLE patenti(
	patenteID BIGINT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO patenti (tipo) VALUES
('A'),
('B'),
('C'),
('D'),
('Patente Nautica'),
('Patente Elicottero');

CREATE TABLE abilita(
	abilitaID BIGINT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO abilita (tipo) VALUES
('Primo Soccorso'),
('Antincendio'),
('Sommozzatore'),
('Alpinismo'),
('Uso Defibrillatore');

CREATE TABLE operatori(
	operatoreID BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    eta DATE,
    email VARCHAR(255),
    UNIQUE(email),
    passkey VARCHAR(255),
	stato VARCHAR(255) DEFAULT 'libero',
    CHECK (stato IN ('libero', 'occupato'))
);



INSERT INTO operatori (nome, cognome, eta, email, passkey, stato) VALUES
('Luca', 'Rossi', '1995-01-01', 'luca.rossi@mail.com', 'luca123', 'occupato'),
('Marco', 'Bianchi', '1980-01-01', 'marco.bianchi@mail.com', 'marco123', 'occupato'),
('Giulia', 'Verdi', '1997-01-01', 'giulia.verdi@mail.com', 'giulia123', 'occupato'),
('Anna', 'Neri', '1990-01-01', 'anna.neri@mail.com', 'anna123', 'occupato'),
('Paolo', 'Gialli', '1985-01-01', 'paolo.gialli@mail.com', 'paolo123', 'occupato'),
('Francesca', 'Marini', '1992-04-15', 'francesca.marini@mail.com', 'francesca123', 'libero'),
('Davide', 'Conti', '1988-07-22', 'davide.conti@mail.com', 'davide123', 'libero'),
('Elena', 'Ferrari', '1999-11-03', 'elena.ferrari@mail.com', 'elena123', 'libero'),
('Simone', 'Romano', '1983-02-18', 'simone.romano@mail.com', 'simone123', 'libero'),
('Chiara', 'Moretti', '1996-09-27', 'chiara.moretti@mail.com', 'chiara123', 'libero'),
('Matteo', 'Ricci', '1979-12-05', 'matteo.ricci@mail.com', 'matteo123', 'libero'),
('Sara', 'Galli', '1994-06-12', 'sara.galli@mail.com', 'sara123', 'libero'),
('Alessandro', 'Lombardi', '1986-03-30', 'alessandro.lombardi@mail.com', 'alessandro123', 'libero'),
('Martina', 'Esposito', '2000-01-25', 'martina.esposito@mail.com', 'martina123', 'libero'),
('Federico', 'Rinaldi', '1991-08-09', 'federico.rinaldi@mail.com', 'federico123', 'libero');

CREATE TABLE amministratori(
	amministratoreID BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    email VARCHAR(255),
    UNIQUE(email),
    passkey VARCHAR(255)
);

INSERT INTO amministratori (nome, email, passkey) VALUES
('Roberto Mancini', 'roberto.mancini@mail.com', 'roberto123'),
('Laura De Angelis', 'laura.deangelis@mail.com', 'laura123'),
('Giorgio Ferri', 'giorgio.ferri@mail.com', 'giorgio123');


       
CREATE TABLE operatori_patenti(
	patenteRIF BIGINT NOT NULL,
    operatoreRIF BIGINT NOT NULL,
    FOREIGN KEY (patenteRIF) REFERENCES patenti (patenteID) ON DELETE CASCADE,
    FOREIGN KEY (operatoreRIF) REFERENCES operatori (operatoreID) ON DELETE CASCADE,
    PRIMARY KEY (patenteRIF, operatoreRIF)
);

INSERT INTO operatori_patenti VALUES
(1,1),
(2,1),
(2,2),
(3,3),
(4,4),
(5,5);

CREATE TABLE operatori_abilita(
	operatoreRIF BIGINT NOT NULL,
    abilitaRIF BIGINT NOT NULL,
    FOREIGN KEY (operatoreRIF) REFERENCES operatori (operatoreID) ON DELETE CASCADE,
    FOREIGN KEY (abilitaRIF) REFERENCES abilita (abilitaID) ON DELETE CASCADE,
    PRIMARY KEY (operatoreRIF, abilitaRIF)
);	

INSERT INTO operatori_abilita VALUES
(1,1),
(1,5),
(2,2),
(3,3),
(4,4),
(5,1);

CREATE TABLE squadre(
	squadraID BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL
);	

INSERT INTO squadre (nome) VALUES
('Squadra Alfa'),
('Squadra Beta');

CREATE TABLE appartenenza(
	appartenenzaID BIGINT PRIMARY KEY AUTO_INCREMENT,
	operatoreRIF BIGINT NOT NULL,
    squadraRIF BIGINT NOT NULL,
    caposquadra BOOLEAN,
	FOREIGN KEY (operatoreRIF) REFERENCES operatori (operatoreID) ON DELETE CASCADE,
    FOREIGN KEY (squadraRIF) REFERENCES squadre (squadraID) ON DELETE CASCADE,
    UNIQUE (operatoreRIF, squadraRIF)
);

INSERT INTO appartenenza (operatoreRIF, squadraRIF, caposquadra) VALUES
(1,1,'1'),
(2,1,'0'),
(3,1,'0'),
(4,2,'1'),
(5,2,'0');



CREATE TABLE richieste(
	richiestaID BIGINT PRIMARY KEY AUTO_INCREMENT,
    nomePERS VARCHAR(255),
    mailPERS VARCHAR(255),
    descrizione TEXT,
    indirizzo VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    working_at DATETIME,
    closed_at DATETIME,
    codice_u VARCHAR(36),
    fase VARCHAR(255) DEFAULT 'pendente',
    CHECK (fase IN ('in esecuzione', 'terminata', 'rifiutata', 'attiva', 'pendente'))
);


INSERT INTO richieste (nomePERS, mailPERS, descrizione, indirizzo, working_at, closed_at, fase) VALUES
('Mario Rossi',  'mario@mail.com', 'Incendio in abitazione', 'Via Roma 10',  '2026-05-20 10:30:00',  NULL, 'in esecuzione'),

('Luigi Verdi', 
 'luigi@mail.com', 
 'Persona dispersa in montagna', 
 'Gran Sasso', 
 '2026-05-18 08:15:00', 
 '2026-05-18 14:45:00', 
 'terminata'),

('Anna Bianchi', 
 'anna@mail.com', 
 'Allagamento cantina', 
 'Via Milano 22', 
 NULL, 
 NULL, 
 'attiva'),

('Giovanni Neri', 
 'giovanni.neri@mail.com', 
 'Falso allarme per fumo in edificio', 
 'Via Napoli 5', 
 NULL, 
 '2026-05-19 12:00:00', 
 'rifiutata'),

('Laura Gialli', 
 'laura.gialli@mail.com', 
 'Incidente stradale con feriti', 
 'Strada Provinciale 12', 
 '2026-05-21 16:20:00', 
 NULL, 
 'in esecuzione'),
 
 ('Mario Rossi',
'mario.rossi@mail.com',
'Richiesta di soccorso per malore improvviso',
'Via Roma 25, L''Aquila',
NULL,
NULL,
'pendente'
);
 
 SELECT * FROM richieste;


CREATE TABLE missioni(
	missioneID bigint primary key auto_increment,
    obiettivo varchar(255),
    posizione varchar(255),
    richiestaRIF bigint not null,
    squadraRIF bigint not null,
	FOREIGN KEY (richiestaRIF) REFERENCES richieste (richiestaID) ON DELETE CASCADE,
    FOREIGN KEY (squadraRIF) REFERENCES squadre (squadraID) ON DELETE CASCADE,
    UNIQUE (richiestaRIF, squadraRIF),
	fase VARCHAR(255) DEFAULT 'attiva',
    CHECK (fase IN ('terminata', 'attiva'))
);

INSERT INTO missioni (obiettivo, posizione, richiestaRIF, squadraRIF, fase) VALUES
('Spegnere incendio', 'Via Roma 10', 1, 1, 'attiva'),
('Soccorso incidente stradale', 'Strada Provinciale 12', 5, 2, 'attiva');

CREATE TABLE comunicazioni(
	comunicazioneID BIGINT PRIMARY KEY AUTO_INCREMENT,
	contenuto TEXT,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ruolo VARCHAR(255) NOT NULL,
	missioneRIF BIGINT NOT NULL,
    FOREIGN KEY(missioneRIF) REFERENCES missioni (missioneID) ON DELETE CASCADE
);


INSERT INTO comunicazioni (contenuto, missioneRIF, ruolo) VALUES 
('test di comunicazione', '1', 'admin');



CREATE TABLE materiali(
	materialeID bigint primary key auto_increment,
    tipo varchar(255),
    seriale CHAR(5) UNIQUE,
	stato VARCHAR(255) DEFAULT 'libero',
    CHECK (stato IN ('libero', 'occupato'))
);

INSERT INTO materiali (tipo, seriale, stato) VALUES
('Estintore', 'MAT01', 'occupato'),
('Corda', 'MAT02', 'libero'),
('Pompa acqua', 'MAT03', 'occupato'),
('Kit medico', 'MAT04', 'occupato'),
('Defibrillatore', 'MAT05', 'occupato'),
('Scala telescopica', 'MAT06', 'libero'),
('Torcia', 'MAT07', 'libero'),
('Radio portatile', 'MAT08', 'libero');
SELECT * FROM materiali;

CREATE TABLE mezzi(
	mezzoID bigint primary key auto_increment,
    tipo varchar(255),
    seriale CHAR(5) UNIQUE,
	stato VARCHAR(255) DEFAULT 'libero',
    CHECK (stato IN ('libero', 'occupato'))
);

INSERT INTO mezzi (tipo, seriale, stato) VALUES
('Ambulanza', 'MEZ01', 'occupato'),
('Elicottero', 'MEZ02', 'libero'),
('Camion', 'MEZ03', 'libero'),
('Gommone', 'MEZ04', 'libero'),
('Fuoristrada', 'MEZ05', 'libero'),
('Autopompa', 'MEZ06', 'occupato'),
('Moto di soccorso', 'MEZ07', 'libero'),
('Furgone logistico', 'MEZ08', 'libero');

CREATE TABLE materiali_missioni(
	materialeRIF BIGINT NOT NULL,
    missioneRIF BIGINT NOT NULL,
    FOREIGN KEY (materialeRIF) REFERENCES materiali (materialeID) ON DELETE CASCADE,
    FOREIGN KEY (missioneRIF) REFERENCES missioni (missioneID) ON DELETE CASCADE,
    PRIMARY KEY (materialeRIF, missioneRIF)
);

INSERT INTO materiali_missioni VALUES
(1,1),
(4,1),
(3,2),
(5,2);

CREATE TABLE mezzi_missioni(
	mezzoRIF BIGINT NOT NULL,
    missioneRIF BIGINT NOT NULL,
    FOREIGN KEY (mezzoRIF) REFERENCES mezzi (mezzoID) ON DELETE CASCADE,
    FOREIGN KEY (missioneRIF) REFERENCES missioni (missioneID) ON DELETE CASCADE,
    PRIMARY KEY (mezzoRIF, missioneRIF)
);

INSERT INTO mezzi_missioni VALUES
(6,1),
(1,2);


