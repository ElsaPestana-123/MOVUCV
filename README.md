# MOVUCV

**MOVUCV** es una aplicación de escritorio desarrollada en **Java Swing** para gestionar el sistema de movilidad y transporte de la Universidad Central de Venezuela. El sistema permite administrar usuarios, rutas, flotas e itinerarios, ofreciendo una experiencia segura y adaptada a distintos roles (estudiantes, trabajadores, público general, conductores y administradores).

## Descripción General

La aplicación está diseñada como una solución integral de gestión para la comunidad universitaria. Sus pilares fundamentales incluyen:

- **Autenticación y Seguridad:** Registro e inicio de sesión de usuarios con validación estricta de datos (cédula, correo, contraseña, formato de nombres).
- **Control de Acceso:** Diferenciación operativa según el rol del usuario y autorización cruzada por cédula para miembros de la comunidad universitaria.
- **Gestión Logística:** Panel administrativo dedicado a la administración de la flota de autobuses y la programación de itinerarios.
- **Persistencia Local:** Almacenamiento y manipulación de datos en tiempo real utilizando archivos de texto plano estructurados.
- **Arquitectura:** Diseño basado en el patrón MVC (Modelo-Vista-Controlador) y DAO (Data Access Object) para la capa de datos.

## 🚀 Funcionalidades Principales

- **Roles Soportados:**
  - `E` = Estudiante
  - `T` = Trabajador
  - `P` = Público general
  - `C` = Conductor
  - `A` = Administrador
- **Validaciones de Negocio:** Prevención de choques de horarios, control de unidades operativas y filtrado estricto de expresiones regulares (RegEx).
- **Paneles Dedicados:** Home personalizado para el usuario final (reservas) y para el administrador (operaciones CRUD).
- **Testing:** Suite de pruebas unitarias implementadas con JUnit para garantizar la integridad de las validaciones y el acceso a datos.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java SE (JDK 17 o superior)
- **Interfaz Gráfica:** Java Swing
- **Testing:** JUnit 4
- **Persistencia:** Archivos `.txt` (I/O local)

## 📂 Estructura del Proyecto

```text
MOVUCV/
├── src/                          # Código fuente Java
│   ├── _helpers/                 # Validaciones y utilidades auxiliares
│   ├── controller/               # Controladores y gestión de eventos
│   ├── model/                    # Modelos de dominio y acceso a datos (DAO)
│   ├── view/                     # Interfaces gráficas (Swing)
│   └── Main.java                 # Punto de entrada de la aplicación
├── data/                         # Archivos de persistencia local
│   ├── comunidad-universitaria-ucv.txt
│   ├── itinerarios.txt
│   ├── unidades.txt
│   └── usuarios.txt
├── lib/                          # Dependencias externas
│   ├── junit-4.13.2.jar
│   └── hamcrest-core-1.3.jar
├── test/                         # Suite de pruebas unitarias
│   ├── UsuarioInicioSesionTest.java
│   └── ItinerarioDAOTest.java
├── res/                          # Recursos visuales y assets del proyecto
├── build/                        # Salida de clases compiladas
├── .gitignore
```
## ⚙ Cómo Ejecutar el Proyecto

### 1. Requisitos Previos
- JDK 17 o superior configurado en las variables de entorno.
- Terminal (PowerShell, CMD o Bash).
- Git (opcional, para control de versiones).

### 2. Compilar y Arrancar (PowerShell)
Desde la carpeta raíz del proyecto, ejecuta los siguientes comandos para compilar el código fuente y lanzar la aplicación:

```powershell
$files = Get-ChildItem -Path '.\src' -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
javac -d '.\build' $files
java -cp '.\build' src.Main
```

## 🧪 Cómo Ejecutar las Pruebas (JUnit)

Para correr la suite de pruebas unitarias y verificar la integridad de los módulos, ejecuta:

```powershell
$srcFiles = Get-ChildItem -Path '.\src' -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
$testFiles = Get-ChildItem -Path '.\test' -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName
javac -cp '.\lib\junit-4.13.2.jar' -d '.\build' $srcFiles $testFiles
$cp = '.\build;.\lib\junit-4.13.2.jar;.\lib\hamcrest-core-1.3.jar'
java -cp $cp org.junit.runner.JUnitCore UsuarioInicioSesionTest
```
## 💾 Convenciones de Datos y Persistencia

La aplicación maneja el estado de la información mediante archivos `.txt` en la carpeta `data/`:
- Los nombres y apellidos se normalizan a mayúsculas al momento del registro.
- Se realizan verificaciones de formato (correo, cédula, horas, contraseñas) antes de cualquier escritura en disco.
- El sistema es capaz de regenerar los archivos de persistencia si estos son eliminados.

## 👥 Autores

- Humberto García
- Elsa Pestana
- Veronica Quintero
