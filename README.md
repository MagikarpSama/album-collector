# Album Collector

Album Collector es una aplicación web para gestionar colecciones de láminas de álbumes (por ejemplo, de Pokémon, fútbol, etc). Permite registrar álbumes, agregar láminas, marcar cuáles tienes y ver cuáles te faltan o tienes repetidas.

## Características principales
- Gestión de álbumes y láminas.
- Estados de lámina: "obtenida" y "no obtenida".
- Cantidad de cada lámina (solo se muestra si tienes más de una).
- Filtros por estado, tipo y álbum.
- Álbum de ejemplo precargado con todas las láminas obtenidas y por obtener.

## Instalación y ejecución

### Requisitos
- Java 17 o superior
- Maven 3.8+
- (Opcional) MySQL o base de datos compatible con Spring Data JPA

### Pasos
1. Clona el repositorio o descarga el código fuente.
2. En la raíz del proyecto, ejecuta:
   ```
   mvn spring-boot:run
   ```
3. Accede a la aplicación en [http://localhost:8080/laminas](http://localhost:8080/laminas)

> **Nota:** Al iniciar por primera vez, se crea un álbum de ejemplo con todas las láminas (obtenidas y no obtenidas) para que puedas probar la app.

## Endpoints principales

### Web (Thymeleaf)
- `/laminas` - Vista principal de láminas, filtros y gestión.
- `/albums` - Gestión de álbumes.

### API REST
- `GET /api/laminas` - Lista todas las láminas.
- `GET /api/laminas/{id}` - Obtiene una lámina por ID.
- `POST /api/laminas` - Crea una nueva lámina (JSON).
- `PUT /api/laminas/{id}` - Actualiza una lámina existente (JSON).
- `DELETE /api/laminas/{id}` - Elimina una lámina.
- `POST /api/laminas/bulk` - Carga masiva de láminas (JSON array).
- `GET /api/laminas/faltantes/{albumId}` - Lista láminas no obtenidas de un álbum.
- `GET /api/laminas/repetidas/{albumId}` - Lista láminas con cantidad > 1 de un álbum.
- `POST /api/laminas/{id}/imagen` - Sube imagen para una lámina.

### Ejemplo de entidad Lámina (JSON)
```json
{
  "nombre": "Pokemon #1",
  "imagen": "https://...",
  "estado": "obtenida",
  "cantidad": 1,
  "tipoLamina": "Común",
  "album": { "id": 1 }
}
```

## Notas adicionales
- El filtro "duplicadas" en la web muestra solo las láminas con cantidad mayor a 1.
- Solo existen los estados "obtenida" y "no obtenida".
- Puedes editar, eliminar y agregar láminas desde la interfaz web.

