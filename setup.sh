#!/bin/bash
set -e

# Install JDK 17
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk

# Install and start MariaDB, create databases
sudo apt-get install -y mariadb-server
sudo sed -i 's/3306/3406/' /etc/mysql/mariadb.conf.d/50-server.cnf
sudo systemctl restart mariadb
mysql -u root <<EOF
CREATE DATABASE IF NOT EXISTS contrato;
CREATE DATABASE IF NOT EXISTS empleado;
CREATE DATABASE IF NOT EXISTS entrenamiento;
CREATE DATABASE IF NOT EXISTS nomina;
CREATE DATABASE IF NOT EXISTS consultas;
CREATE DATABASE IF NOT EXISTS orquestador;
EOF

# Install Docker and Docker Compose
sudo apt-get install -y docker.io docker-compose
sudo systemctl enable --now docker
