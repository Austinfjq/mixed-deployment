

CREATE DATABASE HybridDeployment;

USE HybridDeployment;

CREATE TABLE Service(
	serviceID VARCHAR(50) NOT NULL PRIMARY KEY,
	namespace VARCHAR(50) NOT NULL,
	serviceName VARCHAR(50) NOt NULL,
	clusterIP VARCHAR(50) NOt NULL,
	ownerName VARCHAR(30),
	ownerType VARCHAR(30),
	responseTime VARCHAR(10),
	cpuRequest SMALLINT,
	cpuLimit SMALLINT,
	memLimit SMALLINT,
	memRequest SMALLINT,
	period INT,
	serviceType TINYINT
);
CREATE TABLE serviceLoad_mapping_podInstances(
	mappingID VARCHAR(50) NOT NULL PRIMARY KEY,
	serviceID VARCHAR(50) NOT NULL,
	serviceLoad INT NOT NULL,
	podInstances INT NOT NULL,
	FOREIGN KEY(serviceID) REFERENCES Service(serviceID)
);
CREATE TABLE service_storge_info(
	storgeID VARCHAR(50) NOT NULL PRIMARY KEY,
	serviceID VARCHAR(50) NOT NULL ,
	storgeType VARCHAR(10) NOT NULL,
	storgeValue INT NOT NULL,
	FOREIGN KEY(serviceID) REFERENCES Service(serviceID)
);
CREATE TABLE forecaseCell(
	id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
	cellID VARCHAR(250) NOT NULL ,
	forecastIndex VARCHAR(50) NOt NULL,
	timeInterval INT,
	numberOfPerPeriod INT,
	forecastMode VARCHAR(50),
	modelParams VARCHAR(512),
	forecastEndtime DATE
);

