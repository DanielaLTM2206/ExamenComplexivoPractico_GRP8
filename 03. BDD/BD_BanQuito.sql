-- =====================================================================
-- Universidad de las Fuerzas Armadas ESPE - Arquitectura de Software
-- NRC 36883 - Grupo 08 - Examen Complexivo Práctico
-- Esquema: banquito (Core Bancario BanQuito con Módulo de Crédito)
-- Motor: MySQL 8.0 o superior
-- Las fechas de los movimientos son relativas a CURDATE(), así las reglas
-- de "último mes" y "últimos 3 meses" se cumplen el día en que se ejecute.
-- =====================================================================

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS banquito;
CREATE DATABASE banquito CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE banquito;

-- Usuario de la aplicación: los servidores se conectan con Monster / Monster9 (no con root)
CREATE USER IF NOT EXISTS 'Monster'@'localhost' IDENTIFIED BY 'Monster9';
ALTER USER 'Monster'@'localhost' IDENTIFIED BY 'Monster9';
GRANT SELECT, INSERT, UPDATE, DELETE ON banquito.* TO 'Monster'@'localhost';

-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------
CREATE TABLE CLIENTE (
    cod_cliente      INT          NOT NULL AUTO_INCREMENT COMMENT 'Código interno del cliente',
    cedula           CHAR(10)     NOT NULL COMMENT 'Cédula de identidad (única)',
    nombre           VARCHAR(100) NOT NULL COMMENT 'Nombres y apellidos',
    genero           CHAR(1)      NOT NULL COMMENT 'M = masculino, F = femenino',
    fecha_nacimiento DATE         NOT NULL COMMENT 'Fecha de nacimiento para calcular la edad',
    CONSTRAINT PK_CLIENTE PRIMARY KEY (cod_cliente),
    CONSTRAINT AK_CLIENTE_CEDULA UNIQUE (cedula),
    CONSTRAINT CK_CLIENTE_GENERO CHECK (genero IN ('M', 'F'))
) ENGINE = InnoDB COMMENT = 'Clientes del banco';

CREATE TABLE CUENTA (
    num_cuenta  VARCHAR(10)   NOT NULL COMMENT 'Número de cuenta',
    cod_cliente INT           NOT NULL COMMENT 'Titular de la cuenta',
    saldo       DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT 'Saldo disponible',
    CONSTRAINT PK_CUENTA PRIMARY KEY (num_cuenta),
    CONSTRAINT FK_CUENTA_CLIENTE FOREIGN KEY (cod_cliente) REFERENCES CLIENTE (cod_cliente),
    CONSTRAINT CK_CUENTA_SALDO CHECK (saldo >= 0)
) ENGINE = InnoDB COMMENT = 'Cuentas de ahorro de los clientes';

CREATE TABLE MOVIMIENTO (
    cod_movimiento INT           NOT NULL AUTO_INCREMENT COMMENT 'Código del movimiento',
    num_cuenta     VARCHAR(10)   NOT NULL COMMENT 'Cuenta afectada',
    tipo           CHAR(3)       NOT NULL COMMENT 'DEP = depósito, RET = retiro',
    valor          DECIMAL(12,2) NOT NULL COMMENT 'Valor del movimiento',
    fecha          DATE          NOT NULL COMMENT 'Fecha del movimiento',
    CONSTRAINT PK_MOVIMIENTO PRIMARY KEY (cod_movimiento),
    CONSTRAINT FK_MOVIMIENTO_CUENTA FOREIGN KEY (num_cuenta) REFERENCES CUENTA (num_cuenta),
    CONSTRAINT CK_MOVIMIENTO_TIPO CHECK (tipo IN ('DEP', 'RET')),
    CONSTRAINT CK_MOVIMIENTO_VALOR CHECK (valor > 0)
) ENGINE = InnoDB COMMENT = 'Depósitos y retiros de las cuentas';

CREATE INDEX IX_MOVIMIENTO_CUENTA_FECHA ON MOVIMIENTO (num_cuenta, fecha);

CREATE TABLE CREDITO (
    cod_credito INT           NOT NULL AUTO_INCREMENT COMMENT 'Código del crédito',
    cod_cliente INT           NOT NULL COMMENT 'Cliente beneficiario',
    monto       DECIMAL(12,2) NOT NULL COMMENT 'Monto financiado',
    num_cuotas  INT           NOT NULL COMMENT 'Plazo en meses (3 a 18)',
    tasa_anual  DECIMAL(5,2)  NOT NULL DEFAULT 16.50 COMMENT 'Tasa de interés anual',
    valor_cuota DECIMAL(12,2) NOT NULL COMMENT 'Cuota fija mensual',
    fecha       DATE          NOT NULL COMMENT 'Fecha de concesión',
    estado      VARCHAR(10)   NOT NULL DEFAULT 'ACTIVO' COMMENT 'ACTIVO o CANCELADO',
    CONSTRAINT PK_CREDITO PRIMARY KEY (cod_credito),
    CONSTRAINT FK_CREDITO_CLIENTE FOREIGN KEY (cod_cliente) REFERENCES CLIENTE (cod_cliente),
    CONSTRAINT CK_CREDITO_MONTO CHECK (monto > 0),
    CONSTRAINT CK_CREDITO_CUOTAS CHECK (num_cuotas BETWEEN 3 AND 18),
    CONSTRAINT CK_CREDITO_ESTADO CHECK (estado IN ('ACTIVO', 'CANCELADO'))
) ENGINE = InnoDB COMMENT = 'Créditos otorgados (cuota fija mensual)';

CREATE TABLE AMORTIZACION (
    cod_credito INT           NOT NULL COMMENT 'Crédito al que pertenece la cuota',
    num_cuota   INT           NOT NULL COMMENT 'Número de cuota (1..n)',
    valor_cuota DECIMAL(12,2) NOT NULL COMMENT 'Valor de la cuota',
    interes     DECIMAL(12,2) NOT NULL COMMENT 'Interés de la cuota',
    capital     DECIMAL(12,2) NOT NULL COMMENT 'Abono a capital',
    saldo       DECIMAL(12,2) NOT NULL COMMENT 'Saldo pendiente después de la cuota',
    CONSTRAINT PK_AMORTIZACION PRIMARY KEY (cod_credito, num_cuota),
    CONSTRAINT FK_AMORTIZACION_CREDITO FOREIGN KEY (cod_credito) REFERENCES CREDITO (cod_credito)
) ENGINE = InnoDB COMMENT = 'Tabla de amortización de cada crédito';

-- ---------------------------------------------------------------------
-- Datos de prueba: clientes (cada uno cubre un caso de las reglas RN-01 a RN-05)
-- Para probar CLI-001 (no es cliente) usar la cédula 1717171712 (válida, no registrada).
-- ---------------------------------------------------------------------

-- 1. SÍ es sujeto (hombre de 36 años); tuvo un crédito ya CANCELADO; monto máximo 1440.00
-- 2. SÍ es sujeto (mujer de 23 años: la edad mínima solo aplica a hombres); monto máximo 720.00
-- 3. NO es sujeto: hombre menor de 25 años
-- 4. NO es sujeto: sin depósitos en los últimos 30 días
-- 5. NO es sujeto: tiene un crédito ACTIVO
-- 6. SÍ es sujeto, pero retira más de lo que deposita: monto máximo 0.00
-- 7. SÍ es sujeto con dos cuentas; monto máximo 1980.00
-- 8. NO es sujeto: es cliente pero no tiene cuentas ni depósitos
INSERT INTO CLIENTE (cedula, nombre, genero, fecha_nacimiento) VALUES
    ('1710034065', 'Carlos Andrés Andrade Mora', 'M', '1990-03-14'),
    ('1720045671', 'María Fernanda López Ruiz', 'F', '2003-07-22'),
    ('1705678918', 'Jorge Luis Paredes Vega', 'M', '2004-01-10'),
    ('1712345675', 'Luis Alberto Morales Paz', 'M', '1985-11-05'),
    ('1709876542', 'Pedro José Vásquez Lima', 'M', '1980-06-18'),
    ('1715555551', 'Ana Gabriela Torres Silva', 'F', '1995-09-30'),
    ('1718765439', 'Diego Sebastián Salazar Ríos', 'M', '1998-02-25'),
    ('1723456784', 'Valeria Cedeño Bravo', 'F', '2000-12-03');

-- ---------------------------------------------------------------------
-- Cuentas (el saldo es la suma de depósitos menos retiros de cada cuenta)
-- ---------------------------------------------------------------------
INSERT INTO CUENTA (num_cuenta, cod_cliente, saldo) VALUES
    ('2201000001', 1, 5100.00),
    ('2201000002', 2, 1800.00),
    ('2201000003', 3, 800.00),
    ('2201000004', 4, 3100.00),
    ('2201000005', 5, 2570.00),
    ('2201000006', 6, 1050.00),
    ('2201000007', 7, 4850.00),
    ('2201000008', 7, 2850.00);

-- ---------------------------------------------------------------------
-- Movimientos (los de más de 3 meses no entran en el cálculo del monto máximo)
-- ---------------------------------------------------------------------
-- Cuenta 2201000001 - Carlos Andrés Andrade Mora
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000001', 'DEP', 2000.00, CURDATE() - INTERVAL 150 DAY),
    ('2201000001', 'RET', 900.00, CURDATE() - INTERVAL 120 DAY),
    ('2201000001', 'DEP', 1150.00, CURDATE() - INTERVAL 85 DAY),
    ('2201000001', 'RET', 380.00, CURDATE() - INTERVAL 80 DAY),
    ('2201000001', 'DEP', 1250.00, CURDATE() - INTERVAL 70 DAY),
    ('2201000001', 'RET', 420.00, CURDATE() - INTERVAL 62 DAY),
    ('2201000001', 'DEP', 1200.00, CURDATE() - INTERVAL 50 DAY),
    ('2201000001', 'RET', 400.00, CURDATE() - INTERVAL 41 DAY),
    ('2201000001', 'DEP', 1180.00, CURDATE() - INTERVAL 30 DAY),
    ('2201000001', 'RET', 350.00, CURDATE() - INTERVAL 22 DAY),
    ('2201000001', 'DEP', 1220.00, CURDATE() - INTERVAL 10 DAY),
    ('2201000001', 'RET', 450.00, CURDATE() - INTERVAL 5 DAY);

-- Cuenta 2201000002 - María Fernanda López Ruiz
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000002', 'DEP', 600.00, CURDATE() - INTERVAL 140 DAY),
    ('2201000002', 'DEP', 850.00, CURDATE() - INTERVAL 80 DAY),
    ('2201000002', 'RET', 450.00, CURDATE() - INTERVAL 65 DAY),
    ('2201000002', 'DEP', 950.00, CURDATE() - INTERVAL 45 DAY),
    ('2201000002', 'RET', 550.00, CURDATE() - INTERVAL 35 DAY),
    ('2201000002', 'DEP', 900.00, CURDATE() - INTERVAL 12 DAY),
    ('2201000002', 'RET', 500.00, CURDATE() - INTERVAL 6 DAY);

-- Cuenta 2201000003 - Jorge Luis Paredes Vega
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000003', 'DEP', 400.00, CURDATE() - INTERVAL 100 DAY),
    ('2201000003', 'DEP', 300.00, CURDATE() - INTERVAL 60 DAY),
    ('2201000003', 'RET', 150.00, CURDATE() - INTERVAL 40 DAY),
    ('2201000003', 'DEP', 350.00, CURDATE() - INTERVAL 15 DAY),
    ('2201000003', 'RET', 100.00, CURDATE() - INTERVAL 8 DAY);

-- Cuenta 2201000004 - Luis Alberto Morales Paz
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000004', 'DEP', 1500.00, CURDATE() - INTERVAL 130 DAY),
    ('2201000004', 'DEP', 800.00, CURDATE() - INTERVAL 75 DAY),
    ('2201000004', 'DEP', 700.00, CURDATE() - INTERVAL 60 DAY),
    ('2201000004', 'DEP', 650.00, CURDATE() - INTERVAL 40 DAY),
    ('2201000004', 'RET', 300.00, CURDATE() - INTERVAL 20 DAY),
    ('2201000004', 'RET', 250.00, CURDATE() - INTERVAL 10 DAY);

-- Cuenta 2201000005 - Pedro José Vásquez Lima
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000005', 'DEP', 1000.00, CURDATE() - INTERVAL 160 DAY),
    ('2201000005', 'DEP', 600.00, CURDATE() - INTERVAL 70 DAY),
    ('2201000005', 'RET', 200.00, CURDATE() - INTERVAL 50 DAY),
    ('2201000005', 'DEP', 650.00, CURDATE() - INTERVAL 25 DAY),
    ('2201000005', 'RET', 180.00, CURDATE() - INTERVAL 14 DAY),
    ('2201000005', 'DEP', 700.00, CURDATE() - INTERVAL 3 DAY);

-- Cuenta 2201000006 - Ana Gabriela Torres Silva
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000006', 'DEP', 1500.00, CURDATE() - INTERVAL 110 DAY),
    ('2201000006', 'DEP', 200.00, CURDATE() - INTERVAL 80 DAY),
    ('2201000006', 'RET', 350.00, CURDATE() - INTERVAL 66 DAY),
    ('2201000006', 'DEP', 180.00, CURDATE() - INTERVAL 48 DAY),
    ('2201000006', 'RET', 400.00, CURDATE() - INTERVAL 33 DAY),
    ('2201000006', 'DEP', 220.00, CURDATE() - INTERVAL 18 DAY),
    ('2201000006', 'RET', 300.00, CURDATE() - INTERVAL 9 DAY);

-- Cuenta 2201000007 - Diego Sebastián Salazar Ríos
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000007', 'DEP', 2500.00, CURDATE() - INTERVAL 170 DAY),
    ('2201000007', 'DEP', 1500.00, CURDATE() - INTERVAL 82 DAY),
    ('2201000007', 'RET', 300.00, CURDATE() - INTERVAL 60 DAY),
    ('2201000007', 'DEP', 1600.00, CURDATE() - INTERVAL 37 DAY),
    ('2201000007', 'RET', 450.00, CURDATE() - INTERVAL 16 DAY);

-- Cuenta 2201000008 - Diego Sebastián Salazar Ríos
INSERT INTO MOVIMIENTO (num_cuenta, tipo, valor, fecha) VALUES
    ('2201000008', 'DEP', 800.00, CURDATE() - INTERVAL 120 DAY),
    ('2201000008', 'DEP', 1400.00, CURDATE() - INTERVAL 55 DAY),
    ('2201000008', 'RET', 500.00, CURDATE() - INTERVAL 28 DAY),
    ('2201000008', 'DEP', 1500.00, CURDATE() - INTERVAL 7 DAY),
    ('2201000008', 'RET', 350.00, CURDATE() - INTERVAL 2 DAY);

-- ---------------------------------------------------------------------
-- Créditos: uno CANCELADO (no impide un nuevo crédito) y uno ACTIVO
-- El crédito 2 reproduce el ejemplo del enunciado: 450.00 a 6 cuotas = 78.65
-- ---------------------------------------------------------------------
INSERT INTO CREDITO (cod_cliente, monto, num_cuotas, tasa_anual, valor_cuota, fecha, estado) VALUES
    (1, 299.00, 3, 16.50, 102.42, CURDATE() - INTERVAL 200 DAY, 'CANCELADO'),
    (5, 450.00, 6, 16.50, 78.65, CURDATE() - INTERVAL 20 DAY, 'ACTIVO');

INSERT INTO AMORTIZACION (cod_credito, num_cuota, valor_cuota, interes, capital, saldo) VALUES
    (1, 1, 102.42, 4.11, 98.31, 200.69),
    (1, 2, 102.42, 2.76, 99.66, 101.03),
    (1, 3, 102.42, 1.39, 101.03, 0.00),
    (2, 1, 78.65, 6.19, 72.46, 377.54),
    (2, 2, 78.65, 5.19, 73.46, 304.08),
    (2, 3, 78.65, 4.18, 74.47, 229.61),
    (2, 4, 78.65, 3.16, 75.49, 154.12),
    (2, 5, 78.65, 2.12, 76.53, 77.59),
    (2, 6, 78.66, 1.07, 77.59, 0.00);

-- ---------------------------------------------------------------------
-- Verificación: resultado esperado de las reglas para cada cliente
-- ---------------------------------------------------------------------
SELECT r.cod_cliente, r.cedula, r.nombre, r.genero, r.edad,
       r.deposito_ultimo_mes, r.credito_activo,
       (r.deposito_ultimo_mes = 1 AND (r.genero = 'F' OR r.edad >= 25) AND r.credito_activo = 0) AS es_sujeto,
       GREATEST(ROUND((r.prom_dep - r.prom_ret) * 0.30 * 6, 2), 0) AS monto_maximo
FROM (
    SELECT c.cod_cliente, c.cedula, c.nombre, c.genero,
           TIMESTAMPDIFF(YEAR, c.fecha_nacimiento, CURDATE()) AS edad,
           EXISTS (SELECT 1 FROM MOVIMIENTO m JOIN CUENTA cu ON cu.num_cuenta = m.num_cuenta
                   WHERE cu.cod_cliente = c.cod_cliente AND m.tipo = 'DEP'
                     AND m.fecha >= CURDATE() - INTERVAL 30 DAY) AS deposito_ultimo_mes,
           EXISTS (SELECT 1 FROM CREDITO cr
                   WHERE cr.cod_cliente = c.cod_cliente AND cr.estado = 'ACTIVO') AS credito_activo,
           COALESCE((SELECT AVG(m.valor) FROM MOVIMIENTO m JOIN CUENTA cu ON cu.num_cuenta = m.num_cuenta
                     WHERE cu.cod_cliente = c.cod_cliente AND m.tipo = 'DEP'
                       AND m.fecha >= CURDATE() - INTERVAL 90 DAY), 0) AS prom_dep,
           COALESCE((SELECT AVG(m.valor) FROM MOVIMIENTO m JOIN CUENTA cu ON cu.num_cuenta = m.num_cuenta
                     WHERE cu.cod_cliente = c.cod_cliente AND m.tipo = 'RET'
                       AND m.fecha >= CURDATE() - INTERVAL 90 DAY), 0) AS prom_ret
    FROM CLIENTE c
) r
ORDER BY r.cod_cliente;
