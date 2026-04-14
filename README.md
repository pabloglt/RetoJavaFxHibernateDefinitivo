Gestor de Videojuegos - Hibernate & JavaFX (Versión Definitiva)
Este proyecto es una aplicación de escritorio para la gestión de un inventario de videojuegos y películas utilizando el paradigma de Programación Orientada a Objetos en Java.

🛠 Tecnologías Utilizadas
Java 21 (JDK)

JavaFX 21: Interfaz gráfica de usuario.

Hibernate 5.6: Framework ORM para la persistencia de datos.

MySQL Connector 8.0: Driver para la conexión con la base de datos.

Maven: Gestión de dependencias y construcción del proyecto.

📂 Estructura del Proyecto (Paquetes)
🔵 Lógica y Persistencia (src/main/java)
org.example.GestorVideojuegosHibernateJavaFX.user: Entidad Usuario y repositorio para la gestión de cuentas.

org.example.GestorVideojuegosHibernateJavaFX.movie: Entidad Pelicula (Títulos) y repositorio.

org.example.GestorVideojuegosHibernateJavaFX.copy: Entidad Copia (unidades físicas) y repositorio.

org.example.GestorVideojuegosHibernateJavaFX.services:

AuthService: Gestión de inicio de sesión.

SessionService / SimpleSessionService: Manejo de la sesión de Hibernate.

org.example.GestorVideojuegosHibernateJavaFX.controllers: Controladores de las vistas JavaFX (Login, Main, Añadir/Editar Copia, Nueva Película).

org.example.GestorVideojuegosHibernateJavaFX.utils: Clases de utilidad (JavaFXUtil, DataProvider) e interfaz genérica Repository.

🎨 Recursos y Configuración (src/main/resources)
hibernate.cfg.xml: Configuración de la conexión a MySQL y mapeo de clases.

estilos.css: Hoja de estilos para la interfaz gráfica.

Vistas (FXML):

login-view.fxml: Pantalla de acceso.

main-view.fxml: Panel principal con la tabla de inventario.

añadir-copia-view.fxml / editar-copia-view.fxml: Formularios de gestión de unidades.

nueva-pelicula-view.fxml: Formulario para registrar nuevos títulos.

images/: Iconos de la interfaz (user.png, group.png).

⚙️ Configuración Necesaria
Archivo hibernate.cfg.xml: Se debe ajustar la URL, usuario y contraseña de la base de datos MySQL local.

Dependencias: El archivo pom.xml gestiona automáticamente la descarga de las librerías necesarias mediante Maven.

Punto de Entrada: La ejecución se realiza desde la clase Launcher.java para evitar conflictos con el entorno de ejecución de JavaFX.

🚀 Funcionalidades Incluidas
Inicio de sesión de usuarios.

Listado dinámico de videojuegos/películas y sus copias en una TableView.

Creación de nuevos títulos.

Registro, edición y eliminación (CRUD) de copias asociadas a títulos y usuarios.

Aplicación de estilos visuales mediante CSS.
