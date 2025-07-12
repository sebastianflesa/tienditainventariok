# Tiendita Inventario - Microservicio de Gestión de Stock

Este microservicio consume mensajes del topic `ventas` de Kafka y actualiza automáticamente el stock de productos en una base de datos Oracle.

## Configuración

### Dependencias
- Spring Boot 3.5.3
- Spring Data JPA
- Spring Kafka
- Oracle JDBC Driver
- Jackson (para serialización JSON)
- Lombok

### Base de Datos Oracle
- **URL**: `jdbc:oracle:thin:@localhost:1521:XE`
- **Usuario**: `hr`
- **Contraseña**: `hr`
- **Tabla**: `PRODUCTOS`

### Configuración de Kafka
- **Brokers**: `localhost:29092`, `localhost:39092`
- **Topic**: `ventas`
- **Grupo de consumidores**: `inventario-consumer-group`

## Estructura de la Tabla PRODUCTOS

```sql
CREATE TABLE PRODUCTOS (
    ID NUMBER(19,0) PRIMARY KEY,
    NOMBRE VARCHAR2(255 CHAR) NOT NULL,
    STOCK NUMBER(38,0) DEFAULT 0,
    VALOR NUMBER(10,0) DEFAULT 0
);
```

## Uso

### 1. Configurar la Base de Datos
Ejecuta el script SQL incluido en `src/main/resources/schema.sql`:
```sql
-- Conectarse a Oracle como usuario hr
sqlplus hr/hr@localhost:1521/XE
@schema.sql
```

### 2. Iniciar Kafka
Desde el directorio raíz del proyecto microservicios:
```bash
docker-compose up -d
```

### 3. Iniciar la aplicación
```bash
cd tienditainventario
mvn spring-boot:run
```

La aplicación se ejecutará en el puerto `8081`.

### 4. Probar el servicio

#### Verificar estado del servicio:
```bash
curl http://localhost:8081/api/inventario/health
```

#### Crear productos de ejemplo:
```bash
curl -X POST http://localhost:8081/api/inventario/productos/ejemplo
```

#### Consultar todos los productos:
```bash
curl http://localhost:8081/api/inventario/productos
```

#### Consultar stock de un producto específico:
```bash
curl http://localhost:8081/api/inventario/productos/1/stock
```

#### Crear un nuevo producto:
```bash
curl -X POST http://localhost:8081/api/inventario/productos \
  -H "Content-Type: application/json" \
  -d '{
    "id": 6,
    "nombre": "Webcam HD",
    "stock": 25,
    "valor": 75
  }'
```

## Funcionalidades

### Consumo de Mensajes Kafka
- **Escucha**: Topic `ventas`
- **Procesamiento**: Automático de reducción de stock
- **Confirmación**: Manual para garantizar procesamiento exitoso

### Gestión de Inventario
- **Reducción automática de stock** al recibir ventas
- **Validación de stock suficiente** antes de procesar
- **Transacciones seguras** con rollback en caso de error
- **Logs detallados** para monitoreo

### API REST
- `GET /api/inventario/productos` - Listar todos los productos
- `GET /api/inventario/productos/{id}` - Obtener producto por ID
- `GET /api/inventario/productos/{id}/stock` - Obtener stock de un producto
- `POST /api/inventario/productos` - Crear nuevo producto
- `PUT /api/inventario/productos/{id}` - Actualizar producto
- `POST /api/inventario/productos/ejemplo` - Crear productos de ejemplo

## Flujo de Procesamiento

1. **Recepción**: El servicio recibe un mensaje del topic `ventas`
2. **Deserialización**: Convierte el JSON a objeto `Venta`
3. **Validación**: Verifica que el producto existe y hay stock suficiente
4. **Actualización**: Reduce el stock en la base de datos Oracle
5. **Confirmación**: Confirma el mensaje en Kafka si el procesamiento fue exitoso

## Formato del Mensaje de Venta

```json
{
  "id": "venta-uuid",
  "productoId": "1",
  "clienteId": "cliente-123",
  "cantidad": 2,
  "precio": 25.99,
  "total": 51.98,
  "fechaVenta": "2025-07-10T10:30:00",
  "estado": "CONFIRMADA"
}
```

## Logs y Monitoreo

El servicio genera logs detallados para:
- Mensajes recibidos de Kafka
- Procesamiento de ventas
- Actualizaciones de stock
- Errores y validaciones
- Estado de transacciones

## Manejo de Errores

- **Stock insuficiente**: El mensaje se confirma pero no se procesa la reducción
- **Producto no encontrado**: Se registra el error y se confirma el mensaje
- **Errores de deserialización**: Se confirma el mensaje para evitar reprocessing infinito
- **Errores de base de datos**: Se hace rollback de la transacción
