DROP DATABASE IF EXISTS alquilerAutomobiles;
CREATE DATABASE alquilerAutomobiles;
DROP USER IF EXISTS auto;
CREATE USER auto IDENTIFIED BY 'auto';
GRANT ALL PRIVILEGES ON alquilerAutomobiles.* TO auto WITH GRANT OPTION;
USE alquilerAutomobiles;

CREATE TABLE Automobil (
	id 	INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    marca varchar(20),
    modelo varchar(20),
    matricula varchar(10) unique not null,
    num_ruedas int,
    autonomia int,
    color varchar(15),
    km int,
    combustible char not null,
    plazas int,
    tipo varchar(20),
    enUso boolean
) ENGINE=InnoDB;
CREATE TABLE Cliente (
	id 	INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    /*clausula unique asegura que los valores de dicho campo no se repetiran*/
    DNI int not null unique,
    nombre varchar(150),
    telefono int
) ENGINE=InnoDB;
CREATE TABLE Alquiler (
	id 	INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_cliente integer not null,
    id_coche integer not null,
    /*fecha inicion/fin del alquiler*/
    fechaInicio varchar(30),
    fechaFin varchar(30),
        /*descripcion de la intentcio del cliente sobre el vehiculo*/
    observaciones varchar(10000)
) ENGINE=InnoDB;
create table usuario(
	id  INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	usuario varchar(300)not null  unique default 'a',
	psswd varchar(5000) not null default 'a'
 )ENGINE=InnoDB;
/*ALTER TABLE Alquiler ADD FOREIGN KEY (id_cliente) REFERENCES Cliente(id);
ALTER TABLE Alquiler ADD FOREIGN KEY (id_coche) REFERENCES Automobil(id);*/

insert into Cliente values(0,82993458,"Fulanito Rodriguez","665724152");
insert into Cliente values(0,68433458,"Susana Horia","667725459");
insert into Cliente values(0,49382582,"Paco Potencia","666173842");
insert into Cliente values(0,12993452,"Elsa Rubio","668523958");
insert into Cliente values(0,19725554,"Ines Tabilidad","665845396");
insert into Cliente values(0,46993854,"Elva Jimenez","664785359");
insert into Cliente values(0,92785458,"Aitor Lopez","667824689");

insert into Automobil values(0,'Mazda','rx7','8549VXD',4,350,'rojo',250562,'G',2,'coche',false);
insert into Automobil values(0,'Nissan','r34','5713PFY',4,410,'azul',150562,'G',5,'coche',false);
insert into Automobil values(0,'Suzuki','Hayabusa','7516XDC',2,200,'negro',158293,'G',2,'moto',false);
insert into Automobil values(0,'Volkswagen','Transporter','8425BRM',4,505,'blanco',529345,'D',3,'furgoneta',false);
insert into Automobil values(0,'Dodge','Viper','2853QHQ',4,260,'verde',80356,'G',5,'coche',true);
insert into Automobil values(0,'Tesla','Roadster','5719NTM',4,380,'rosa',95128,'E',2,'coche',false);
insert into Automobil values(0,'Nissan','Silvia','7548EEM',4,264,'verde',106052,'D',4,'coche',false);
insert into Automobil values(0,'Bmw','e92','8436RPX',4,382,'Plata',92450,'D',4,'coche',false);
insert into Automobil values(0,'Nissan','350z','8519FVM',4,197,'Negro',82827,'D',4,'coche',false);
insert into Automobil values(0,'Bmw','316','8492ZGZ','4','281','Gris','105000','D',2,'coche',false);
insert into Automobil values(0,'Mitsubishi','Evo6','8416OSL',4,312,'amarillo',103000,'G',4,'coche',false);
insert into Automobil values(0,'Tesla','Roadster','5019NUM',4,380,'rosa',95128,'E',2,'coche',false);
insert into Automobil values(0,'Toyota','Prius','5067UAP',4,416,'azul',91008,'H',5,'coche',false);
insert into Automobil values(0,'Ducati','Monster','8097ZJW','4','281','Gris','105000','D',2,'coche',false);
insert into Automobil values(0,'Toyota','Supra','2904FMQ','4','221','negro','20141','G',4,'coche',false);

insert into Alquiler values (0,2,5,'2020-03-31','2020-06-29','El cliente quiere el coche para visitar a un familiar');
insert into Alquiler values (0,3,4,'2019-08-06','2019-08-10','El cliente quiere el coche buscar a un amigo en el aeropuerto');
insert into Alquiler values (0,1,3,'2018-01-04','2018-01-06','El cliente quiere el coche para una escapada de fin de semana');
insert into Alquiler values (0,4,1,'2010-01-24','2010-02-10','El cliente quiere el coche para ir de vacaciones a Paris');
insert into Alquiler values (0,5,2,'2016-05-12','2016-05-23','El vehiculo estaba en perfecto estado antes de que el cliente se lo llevara');
insert into Alquiler values (0,6,6,'2019-01-02','2019-01-12','El vehiculo tenia un golpe en el lateral derecho');
insert into Alquiler values (0,7,7,'2020-03-30','2020-04-29','El vehiculo tiene una falla de pintura en la puerta trasera derecha');

insert into usuario values (0,'a','0cc175b9c0f1b6a831c399e269772661');

select * from alquilerAutomobiles.Cliente;
select * from alquilerAutomobiles.Alquiler;
select * from alquilerAutomobiles.Automobil;
select * from usuario;