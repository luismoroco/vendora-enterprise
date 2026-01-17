# Image Upload System - Vendora Enterprise

## Overview

Sistema de carga de imágenes local implementado para almacenar imágenes de productos, marcas y categorías sin necesidad de servicios externos como S3.

## Arquitectura

### Backend (Spring Boot)

**Endpoint**: `/api/v1/images`

#### Rutas disponibles:

1. **Upload Image**
   - **POST** `/api/v1/images/upload`
   - Content-Type: `multipart/form-data`
   - Request: `file` (MultipartFile)
   - Response: 
     ```json
     {
       "url": "http://localhost:8080/api/v1/images/uuid.jpg",
       "filename": "uuid.jpg"
     }
     ```

2. **Get Image**
   - **GET** `/api/v1/images/{filename}`
   - Response: Image binary with correct Content-Type

3. **Delete Image**
   - **DELETE** `/api/v1/images/{filename}`
   - Response:
     ```json
     {
       "message": "Image deleted successfully"
     }
     ```

#### Configuración

En `application.yml`:
```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 10MB

app:
  upload:
    dir: uploads/images  # Directorio relativo al proyecto
  base:
    url: http://localhost:8080  # URL base para generar URLs de imágenes
```

#### Almacenamiento

- **Directorio**: `module/vendora-backend/uploads/images/`
- **Nombres**: UUID aleatorio + extensión original
- **Validaciones**:
  - Solo archivos de imagen (image/*)
  - Tamaño máximo: 10MB
  - Tipos soportados: PNG, JPG, GIF, WEBP, etc.

### Frontend (Next.js)

#### Componente ImageUpload

**Ubicación**: `app/components/image-upload.tsx`

**Props**:
```typescript
interface ImageUploadProps {
  label?: string        // Label del campo
  value?: string        // URL actual de la imagen
  onChange: (url: string) => void  // Callback cuando cambia la imagen
  disabled?: boolean    // Deshabilitar el componente
}
```

**Características**:
- Preview de la imagen antes de subir
- Drag & drop support (futuro)
- Validación de tipo de archivo
- Validación de tamaño (10MB)
- Indicador de carga
- Botón para remover imagen
- Toast notifications para feedback

#### Servicio de API

**Ubicación**: `lib/services/imageService.ts`

```typescript
imageService.uploadImage(file: File): Promise<{ url: string, filename: string }>
imageService.deleteImage(filename: string): Promise<void>
```

## Uso en Formularios

### Ejemplo: Brand Form

```tsx
import ImageUpload from "./image-upload"

export default function BrandFormDialog() {
  const [imageUrl, setImageUrl] = useState("")

  return (
    <form>
      <ImageUpload
        label="Brand Image"
        value={imageUrl}
        onChange={setImageUrl}
        disabled={loading}
      />
    </form>
  )
}
```

### Formularios Actualizados

- ✅ Brand Form
- ✅ Category Form
- ✅ Product Form

## Estructura de Archivos

```
vendora-enterprise/
├── module/
│   ├── vendora-backend/
│   │   ├── uploads/           # 📁 Directorio de almacenamiento (git ignored)
│   │   │   └── images/
│   │   │       └── {uuid}.jpg
│   │   └── src/
│   │       └── main/
│   │           ├── java/
│   │           │   └── com/vendora/backend/web/
│   │           │       └── ImageController.java
│   │           └── resources/
│   │               └── application.yml
│   └── vendora-pos/
│       ├── app/
│       │   └── components/
│       │       └── image-upload.tsx
│       └── lib/
│           └── services/
│               └── imageService.ts
```

## Configuración Inicial

### 1. Backend

El directorio `uploads/images` se crea automáticamente al subir la primera imagen.

Ya está configurado en `.gitignore`:
```
uploads/
```

### 2. Frontend

No requiere configuración adicional. El componente está listo para usar.

### 3. Variables de Entorno

Backend usa valores por defecto, pero puedes personalizarlos:

```yaml
# application.yml
app:
  upload:
    dir: /ruta/personalizada/imagenes  # Ruta absoluta o relativa
  base:
    url: http://tu-dominio.com  # URL base de producción
```

## Seguridad

### Validaciones Implementadas

1. **Tipo de archivo**: Solo imágenes permitidas
2. **Tamaño**: Máximo 10MB
3. **Extensiones**: Preservadas del archivo original
4. **Nombres únicos**: UUID para evitar colisiones

### Mejoras Futuras

- [ ] Redimensionamiento automático de imágenes
- [ ] Compresión de imágenes
- [ ] Thumbnails automáticos
- [ ] Límite de imágenes por entidad
- [ ] Limpieza de imágenes huérfanas
- [ ] Validación de dimensiones
- [ ] Protección contra inyección de archivos maliciosos

## Testing

### Probar Backend (curl)

```bash
# Subir imagen
curl -X POST http://localhost:8080/api/v1/images/upload \
  -F "file=@/path/to/image.jpg"

# Obtener imagen
curl http://localhost:8080/api/v1/images/{filename} --output image.jpg

# Eliminar imagen
curl -X DELETE http://localhost:8080/api/v1/images/{filename}
```

### Probar Frontend

1. Ir a cualquier formulario (Brands, Categories, Products)
2. Hacer clic en "Upload Image"
3. Seleccionar una imagen
4. Ver el preview y la URL generada

## Troubleshooting

### Error: "File is empty"
- Asegúrate de que el archivo se está enviando correctamente
- Verifica el content-type en la request

### Error: "Failed to upload image"
- Verifica permisos de escritura en el directorio `uploads/`
- Revisa los logs del backend para más detalles

### Imagen no se muestra
- Verifica que la URL esté correcta
- Asegúrate de que el backend esté corriendo
- Revisa CORS si estás en desarrollo

### Tamaño de archivo excedido
- Ajusta `spring.servlet.multipart.max-file-size` en application.yml
- Actualiza la validación en el frontend

## Migración a Producción

### Opción 1: Mantener Almacenamiento Local

1. Usar un volumen persistente en Docker/K8s
2. Configurar backups regulares del directorio `uploads/`
3. Considerar NFS para múltiples instancias

### Opción 2: Migrar a S3/Cloud Storage

Cuando estés listo:
1. Implementar `ImageStorageService` interface
2. Crear implementación S3 o similar
3. Migrar imágenes existentes
4. Actualizar configuración

## Rendimiento

- Las imágenes se sirven directamente desde el filesystem
- No hay procesamiento en cada request
- Considera agregar CDN en producción
- Implementar caché de navegador con headers apropiados

