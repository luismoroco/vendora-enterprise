# 🔧 Configuración de Liquibase con R2DBC - SOLUCIONADO

## 📋 Problema Identificado

Tu aplicación usa **R2DBC (reactivo)** para operaciones de base de datos, pero **Liquibase requiere JDBC tradicional** para ejecutar las migraciones. El problema era que no había un DataSource JDBC configurado explícitamente.

## ✅ Cambios Realizados

### 1. Creado `LiquibaseConfig.java`
**Ubicación:** `applications/app-service/src/main/java/com/vendora/core/config/LiquibaseConfig.java`

Esta clase:
- ✅ Crea un `DataSource` JDBC exclusivo para Liquibase
- ✅ Lee las variables de entorno del archivo `.env`
- ✅ Configura `SpringLiquibase` manualmente con todas las propiedades
- ✅ Usa valores por defecto si no hay variables de entorno

### 2. Actualizado `application.yaml`
**Cambios:**
- ✅ Agregada configuración `spring.datasource.*` para JDBC
- ✅ Mantenida configuración `spring.liquibase.*` 
- ✅ Configuración R2DBC permanece intacta en `adapters.r2dbc.*`

### 3. Eliminado archivo redundante
- ❌ Eliminado `db/config/liquibase.properties` (ya no se necesita)

## 🚀 Cómo Funciona Ahora

```
┌─────────────────────────────────────────┐
│  Tu Aplicación Spring Boot              │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────────┐   ┌──────────────┐   │
│  │  Liquibase   │   │  R2DBC Pool  │   │
│  │  (JDBC)      │   │  (Reactivo)  │   │
│  └──────┬───────┘   └──────┬───────┘   │
│         │                  │           │
└─────────┼──────────────────┼───────────┘
          │                  │
          └─────┬────────────┘
                │
        ┌───────▼────────┐
        │  PostgreSQL DB │
        └────────────────┘
```

- **Liquibase (JDBC)**: Se ejecuta AL INICIO para crear/actualizar tablas
- **R2DBC**: Se usa en RUNTIME para tus operaciones reactivas normales

## 📦 Verificar Dependencias

Asegúrate de que tu `build.gradle` tiene:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'     // ✅ Para Liquibase
    implementation 'org.liquibase:liquibase-core'                         // ✅ Liquibase
    runtimeOnly 'org.postgresql:postgresql'                               // ✅ Driver JDBC
    
    implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc' // ✅ Para R2DBC
    implementation 'org.postgresql:r2dbc-postgresql'                      // ✅ Driver R2DBC
}
```

## 🔐 Variables de Entorno (.env)

Tu archivo `.env` en la raíz debe tener:

```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=vendora
DB_USER=admin
DB_PASSWORD=admin123
```

## 🎯 Comandos para Ejecutar

### 1. Limpiar y reconstruir
```bash
cd /Users/lmorocoensolvers/Documents/work/vendora/vendora-enterprise/module/vendora-core
./gradlew clean build
```

### 2. Ejecutar la aplicación
```bash
./gradlew :app-service:bootRun
```

### 3. Verificar que funciona
Busca en los logs estas líneas:

```
✅ Liquibase: Running Changelog db/changelog/db.changelog-master.yaml
✅ Successfully acquired change log lock
✅ Creating database history table with name: public.databasechangelog
✅ Reading from public.databasechangelog
✅ Liquibase has updated your database
```

## 🗂️ Estructura de Migraciones

```
applications/app-service/src/main/resources/
└── db/
    └── changelog/
        ├── db.changelog-master.yaml      # Master file (incluye todos)
        └── changes/
            ├── 001-update-trigger.yaml
            ├── 002-trigram-extension.yaml
            ├── 003-provider.yaml
            ├── 004-product.yaml
            ├── 005-shopping-cart.yaml
            ├── 006-brand.yaml
            ├── 007-product-category.yaml
            ├── 008-product-table-add-cost-description-column.yaml
            └── 009-tenant.yaml
```

## 🐛 Solución de Problemas

### Si sigue sin funcionar:

1. **Verifica la base de datos esté corriendo:**
   ```bash
   psql -h localhost -U admin -d vendora
   ```

2. **Verifica las variables de entorno:**
   ```bash
   cat .env
   ```

3. **Revisa los logs en detalle:**
   Busca errores relacionados con:
   - `Liquibase`
   - `DataSource`
   - `Connection refused`

4. **Habilita logs de debug de Liquibase:**
   Agrega a `application.yaml`:
   ```yaml
   logging:
     level:
       liquibase: DEBUG
   ```

## 📊 Tablas que se Crearán

Después de ejecutar, Liquibase creará:

1. `databasechangelog` - Historial de migraciones ejecutadas
2. `databasechangeloglock` - Lock para prevenir ejecuciones concurrentes
3. `provider` - Tu tabla de proveedores
4. `product` - Tu tabla de productos
5. `shopping_cart` - Tu tabla de carritos
6. `brand` - Tu tabla de marcas
7. `product_category` - Tu tabla de categorías
8. `tenant` - Tu tabla de tenants

## ✨ Ventajas de esta Configuración

- ✅ **Separación de responsabilidades**: JDBC para migraciones, R2DBC para runtime
- ✅ **No bloquea el modelo reactivo**: Tu app sigue siendo 100% reactiva
- ✅ **Migraciones automáticas**: Se ejecutan al inicio sin intervención manual
- ✅ **Versionamiento de BD**: Control total del esquema con Liquibase
- ✅ **Rollback capability**: Puedes revertir cambios si es necesario

---

**Nota**: Si encuentras algún problema, revisa los logs completos de la aplicación. Liquibase te dará mensajes muy claros sobre qué está fallando.

