# ms-core-data

**Microservicio de persistencia y orquestación de datos de la plataforma de riesgo crediticio RIntellix.**

`Java 17` · `Spring Boot 4` · `MongoDB` · `Apache Kafka` · `Arquitectura Hexagonal`

---

## 1. Descripción general

`ms-core-data` es el sistema de registro (*system of record*) de RIntellix. Almacena todas las
entidades persistidas relacionadas con una solicitud de crédito: la propia solicitud, la parte
asociada, las simulaciones de riesgo generadas, los resultados de scoring y los informes. El
resto de microservicios nunca acceden directamente a la base de datos: leen y escriben a través
de la API REST de este servicio o intercambian eventos con él mediante Kafka.

En resumen, sus responsabilidades son:

- Exponer endpoints REST tipo CRUD para **solicitudes**, **simulaciones** e **informes**.
- Persistir los datos de dominio en **MongoDB**.
- Consumir los eventos de scoring publicados por `ms-risk-engine` (vía Kafka) y persistir el
  resultado del scoring.
- Servir los ficheros de informe (PDF) generados por `ms-reporting`.

## 2. Aspectos clave del sistema

- **Arquitectura hexagonal (puertos y adaptadores).** El código se organiza en `domain`
  (entidades, enumerados, excepciones y puertos de salida), `application` (casos de uso, puertos
  de entrada, DTOs y mappers) e `infrastructure` (controladores REST, consumidor Kafka,
  repositorios y entidades de MongoDB). Esto mantiene las reglas de negocio independientes de
  Spring, MongoDB o Kafka.
- **Persistencia del scoring dirigida por eventos.** `ScoringKafkaConsumer` escucha los eventos
  de scoring publicados por `ms-risk-engine` y los persiste a través de la capa de dominio,
  desacoplando el motor de cálculo del motor de almacenamiento.
- **Patrón Strategy en los adaptadores de salida.** `infrastructure/adapters/output/strategies`
  permite variar el comportamiento de persistencia/notificación según el tipo de producto sin
  añadir lógica condicional en los casos de uso.
- **Proyecciones de solo lectura.** `infrastructure/projections` expone proyecciones ligeras de
  MongoDB para las vistas de listado/detalle, evitando recuperar documentos completos
  innecesariamente.
- **Endpoints reactivos donde es necesario.** El servicio combina `spring-boot-starter-web`
  (MVC) con `spring-boot-starter-webflux` para los endpoints que se benefician de un estilo
  reactivo/no bloqueante (p. ej., el streaming de ficheros al descargar informes).

### Recursos REST principales

| Recurso | Ruta base | Notas |
|---|---|---|
| Solicitudes | `/api/requests` | Listado, detalle, parte asociada, actualización (`PUT /api/requests/{requestId}`) |
| Simulaciones | `/api/simulations` | CRUD completo + actualización parcial (`PATCH`) |
| Scoring | `/api/requests/{requestId}/scoring` | Lectura del resultado de scoring de una solicitud |
| Informes | `/api/reports` | Creación, listado, filtro por `requestId`, descarga de fichero (`GET /{reportId}/file`) |

### Estructura del repositorio

El siguiente esquema ilustra la distribución del código fuente y cómo las piezas clave de la arquitectura descrita encajan en las carpetas principales del proyecto:

![Estructura de directorios](./estructura_directorios_ms_core_data.svg)

## 3. Tecnologías

- **Lenguaje / runtime:** Java 17
- **Framework:** Spring Boot 4 (`spring-boot-starter-web`, `spring-boot-starter-webflux`,
  `spring-boot-starter-validation`, `spring-boot-starter-actuator`)
- **Persistencia:** `spring-boot-starter-data-mongodb` (MongoDB)
- **Mensajería:** `spring-kafka`
- **Utilidades:** Lombok, Jackson

## 4. Requisitos previos

- JDK 17 o superior
- Maven 3.9+ (o el `mvnw` incluido, si existe)
- Docker (recomendado, mediante `spring-boot-docker-compose` para MongoDB/Kafka en local)
- Una instancia de MongoDB en ejecución y un broker de Apache Kafka accesibles por el servicio

## 5. Puesta en marcha

```bash
# 1. Clonar el repositorio
git clone https://github.com/TFG-RIntellix/ms-core-data.git
cd ms-core-data

# 2. Proporcionar la configuración
# application.yaml / application.properties están excluidos del control de versiones.
# Crea src/main/resources/application.yaml con la URI de MongoDB, los servidores
# bootstrap de Kafka y los nombres de los topics (ver "Configuración" más abajo).

# 3. Ejecutar con Maven
mvn spring-boot:run

# — o compilar y ejecutar el jar —
mvn package -DskipTests
java -jar target/ms-core-data-0.0.1-SNAPSHOT.jar

# — o construir la imagen Docker —
docker build -t ms-core-data .
docker run -p 8081:8081 ms-core-data
```

El servicio escucha por defecto en el **puerto 8081** (ver `Dockerfile`).

## 6. Configuración

Como `application.yaml*` / `application.properties*` están excluidos del repositorio
(`.gitignore`), debes aportar tu propio fichero de configuración con, como mínimo:

- URI de conexión a MongoDB y nombre de la base de datos
- Servidores bootstrap de Kafka y el/los nombre(s) del topic de scoring consumido/producido
- Puerto del servidor (por defecto, `8081`)

Si utilizas `spring-boot-docker-compose`, un `compose.yaml` local (también excluido del
repositorio) puede levantar MongoDB/Kafka automáticamente al ejecutar `mvn spring-boot:run`.

## 7. Servicios relacionados

- **ms-risk-engine** — publica los eventos de scoring que consume este servicio y lo invoca
  (mediante Feign) para leer datos de solicitudes/scoring.
- **ms-reporting** — lee los metadatos de informe de este servicio y almacena a través de él el
  PDF generado.
- **ms-sec-gateway** — enruta el tráfico externo hacia este servicio y aplica la autenticación.

## 8. Autora

Lucía Fernández Mancebo — TFG *RIntellix*, Universidad de Cantabria.



