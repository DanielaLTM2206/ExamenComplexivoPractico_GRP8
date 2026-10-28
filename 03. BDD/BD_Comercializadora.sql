-- =====================================================================
-- Universidad de las Fuerzas Armadas ESPE - Arquitectura de Software
-- NRC 36883 - Grupo 08 - Examen Complexivo Práctico
-- Esquema: comercializadora (catálogo de celulares y facturación)
-- Motor: MySQL 8.0 o superior
-- =====================================================================

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS comercializadora;
CREATE DATABASE comercializadora CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE comercializadora;

-- Usuario de la aplicación: los servidores se conectan con Monster / Monster9 (no con root)
CREATE USER IF NOT EXISTS 'Monster'@'localhost' IDENTIFIED BY 'Monster9';
ALTER USER 'Monster'@'localhost' IDENTIFIED BY 'Monster9';
GRANT SELECT, INSERT, UPDATE, DELETE ON comercializadora.* TO 'Monster'@'localhost';

-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------
CREATE TABLE CELULAR (
    cod_celular INT           NOT NULL AUTO_INCREMENT COMMENT 'Código del celular',
    marca       VARCHAR(40)   NOT NULL COMMENT 'Marca',
    modelo      VARCHAR(60)   NOT NULL COMMENT 'Modelo',
    precio      DECIMAL(10,2) NOT NULL COMMENT 'Precio de venta',
    activo      TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '1 = visible en el catálogo',
    CONSTRAINT PK_CELULAR PRIMARY KEY (cod_celular),
    CONSTRAINT CK_CELULAR_PRECIO CHECK (precio > 0)
) ENGINE = InnoDB COMMENT = 'Catálogo de celulares (sin inventario)';

CREATE TABLE FACTURA (
    num_factura INT           NOT NULL AUTO_INCREMENT COMMENT 'Número de factura',
    cedula      CHAR(10)      NOT NULL COMMENT 'Cédula del cliente',
    nombre      VARCHAR(100)  NOT NULL COMMENT 'Nombre del cliente',
    fecha       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de emisión',
    forma_pago  VARCHAR(10)   NOT NULL COMMENT 'EFECTIVO o CREDITO',
    subtotal    DECIMAL(12,2) NOT NULL COMMENT 'Suma de los detalles',
    descuento   DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '42 % si el pago es en efectivo',
    total       DECIMAL(12,2) NOT NULL COMMENT 'Subtotal menos descuento',
    cod_credito INT           NULL COMMENT 'Crédito otorgado por BanQuito (solo pago a crédito)',
    CONSTRAINT PK_FACTURA PRIMARY KEY (num_factura),
    CONSTRAINT CK_FACTURA_FORMA_PAGO CHECK (forma_pago IN ('EFECTIVO', 'CREDITO')),
    CONSTRAINT CK_FACTURA_CREDITO CHECK ((forma_pago = 'CREDITO') = (cod_credito IS NOT NULL))
) ENGINE = InnoDB COMMENT = 'Cabecera de la factura';

CREATE TABLE DETALLE_FACTURA (
    num_factura     INT           NOT NULL COMMENT 'Factura',
    cod_celular     INT           NOT NULL COMMENT 'Celular vendido',
    cantidad        INT           NOT NULL COMMENT 'Cantidad',
    precio_unitario DECIMAL(10,2) NOT NULL COMMENT 'Precio al momento de la venta',
    subtotal        DECIMAL(12,2) NOT NULL COMMENT 'Cantidad por precio unitario',
    CONSTRAINT PK_DETALLE_FACTURA PRIMARY KEY (num_factura, cod_celular),
    CONSTRAINT FK_DETALLE_FACTURA FOREIGN KEY (num_factura) REFERENCES FACTURA (num_factura),
    CONSTRAINT FK_DETALLE_CELULAR FOREIGN KEY (cod_celular) REFERENCES CELULAR (cod_celular),
    CONSTRAINT CK_DETALLE_CANTIDAD CHECK (cantidad > 0)
) ENGINE = InnoDB COMMENT = 'Celulares facturados';

-- ---------------------------------------------------------------------
-- Catálogo de celulares (el último está inactivo y no debe mostrarse)
-- ---------------------------------------------------------------------
INSERT INTO CELULAR (marca, modelo, precio, activo) VALUES
    ('Samsung', 'Galaxy A16 128GB', 199.00, 1),
    ('Samsung', 'Galaxy A35 256GB', 450.00, 1),
    ('Samsung', 'Galaxy A56 256GB', 529.00, 1),
    ('Samsung', 'Galaxy S25 256GB', 999.00, 1),
    ('Apple', 'iPhone 15 128GB', 829.00, 1),
    ('Apple', 'iPhone 16 128GB', 1049.00, 1),
    ('Apple', 'iPhone 16 Pro 256GB', 1399.00, 1),
    ('Xiaomi', 'Redmi Note 14 256GB', 299.00, 1),
    ('Xiaomi', 'Poco X7 Pro 256GB', 379.00, 1),
    ('Motorola', 'Moto G85 256GB', 289.00, 1),
    ('Motorola', 'Edge 50 Fusion 256GB', 419.00, 1),
    ('Honor', 'Magic7 Lite 256GB', 349.00, 1),
    ('Huawei', 'Nova 12i 128GB', 259.00, 0);

-- ---------------------------------------------------------------------
-- Facturas de ejemplo, coherentes con los créditos del esquema banquito:
--   1 y 2 a crédito (créditos 1 y 2 del Core), 3 en efectivo con 42 % de descuento
-- ---------------------------------------------------------------------
INSERT INTO FACTURA (cedula, nombre, fecha, forma_pago, subtotal, descuento, total, cod_credito) VALUES
    ('1710034065', 'Carlos Andrés Andrade Mora', NOW() - INTERVAL 200 DAY, 'CREDITO', 299.00, 0.00, 299.00, 1),
    ('1709876542', 'Pedro José Vásquez Lima', NOW() - INTERVAL 20 DAY, 'CREDITO', 450.00, 0.00, 450.00, 2),
    ('1715555551', 'Ana Gabriela Torres Silva', NOW() - INTERVAL 4 DAY, 'EFECTIVO', 488.00, 204.96, 283.04, NULL);

INSERT INTO DETALLE_FACTURA (num_factura, cod_celular, cantidad, precio_unitario, subtotal) VALUES
    (1, 8, 1, 299.00, 299.00),
    (2, 2, 1, 450.00, 450.00),
    (3, 1, 1, 199.00, 199.00),
    (3, 10, 1, 289.00, 289.00);
