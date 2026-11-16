CREATE TABLE IF NOT EXISTS usuarios (
  usuario    VARCHAR(30)  NOT NULL PRIMARY KEY,
  nombre     VARCHAR(80)  NOT NULL,
  rol        VARCHAR(20)  NOT NULL,
  clave_hash VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS pqrs (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  radicado        VARCHAR(30)   NOT NULL UNIQUE,
  tipo            VARCHAR(20)   NOT NULL,
  asunto          VARCHAR(120)  NOT NULL,
  descripcion     VARCHAR(1500) NOT NULL,
  barrio          VARCHAR(80),
  ciudadano       VARCHAR(30)   NOT NULL,
  fecha           TIMESTAMP     NOT NULL,
  estado          VARCHAR(20)   NOT NULL,
  respuesta       VARCHAR(1500),
  fecha_respuesta TIMESTAMP     NULL,
  respondido_por  VARCHAR(30)
)
