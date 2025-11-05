# Sistema de Glow Up

Sistema de gestión para tienda de cosméticos desarrollado en Java con persistencia mediante Serializable.

## 📋 Descripción

Sistema de gestión de comercio electrónico para una tienda de cosméticos que permite:
- Gestión de usuarios (clientes, administradores, desarrolladores)
- Catálogo de productos y categorías
- Carrito de compras y compras
- Métodos de pago
- Gestión de fábricas y trabajadores

## 🚀 Requisitos

- Java 8 o superior
- Maven (para gestión de dependencias)

## 📦 Instalación

1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd CabritaSakura
```

2. Compilar el proyecto:
```bash
mvn clean compile
```

3. Ejecutar el programa:
```bash
mvn exec:java -Dexec.mainClass="main.Main"
```

O desde tu IDE favorito ejecutando la clase `Main.java`.

## 👥 Usuarios de Prueba

El sistema incluye los siguientes usuarios de ejemplo (crear a través del menú "Crear Usuario"):

### Dueña
- **Email**: sakura@glow.com
- **Contraseña**: ClaveMaestra
- **Rol**: Dueña (acceso completo a todos los módulos)

## 🎯 Funcionalidades Principales

### Para Clientes
- Crear cuenta
- Añadir métodos de pago
- Ver catálogo de productos
- Añadir productos al carrito
- Realizar compras
- Ver historial de compras
- Gestionar perfil

### Para Administradores de Contenido
- Crear, editar y eliminar productos
- Crear, editar y eliminar categorías
- Gestionar catálogo

### Para Administradores de Usuario
- Activar/suspender cuentas de usuario
- Gestionar usuarios del sistema

### Para la Dueña
- Acceso a todos los módulos
- Administración de usuarios
- Administración de contenido
- Acceso al Consejo Sombrio
- Gestión de registro de esclavos
- Funcionalidades de cliente y desarrollador

## 💾 Persistencia

El sistema guarda automáticamente todos los datos en archivos `.ser`:
- `usuarios.ser` - Usuarios del sistema
- `productos.ser` - Catálogo de productos
- `categorias.ser` - Categorías de productos
- `fabricas.ser` - Fábricas registradas
- `trabajadores.ser` - Trabajadores
- `registro.ser` - Registro de esclavos

Los datos se cargan automáticamente al iniciar el programa y se guardan al salir.

## 🏗️ Estructura del Proyecto

```
src/main/java/
├── main/
│   └── Main.java              # Clase principal con menús y lógica
├── model/
│   ├── comercial/             # Clases de dominio comercial
│   │   ├── Carrito.java
│   │   ├── Categoria.java
│   │   ├── Compra.java
│   │   ├── MetodoDePago.java
│   │   └── Producto.java
│   ├── usuario/               # Tipos de usuarios
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── AdministradorContenido.java
│   │   ├── AdministradorUsuario.java
│   │   ├── DesarrolladorProducto.java
│   │   └── Duena.java
│   ├── enums/                 # Enumeraciones
│   ├── oscuro/                # Módulos especiales
│   └── produccion/            # Gestión de producción
└── exception/                 # Excepciones personalizadas
```

## 📝 Notas

- Todos los datos se persisten automáticamente usando Serializable
- El sistema requiere autenticación para acceder a las funcionalidades
- Algunas funcionalidades requieren permisos especiales (clave maestra)
- La clave maestra por defecto es: `Cabrita2025`

## 👨‍💻 Desarrollo

Proyecto desarrollado como parte del curso de Programación Orientada a Objetos (POO).

## 📄 Licencia

Este proyecto es educativo y de uso académico.

