package org.example.GestorVideojuegosHibernateJavaFX.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.GestorVideojuegosHibernateJavaFX.copy.Copia;
import org.example.GestorVideojuegosHibernateJavaFX.movie.Pelicula;
import org.example.GestorVideojuegosHibernateJavaFX.movie.PeliculaRepository;
import org.example.GestorVideojuegosHibernateJavaFX.services.SimpleSessionService;
import org.example.GestorVideojuegosHibernateJavaFX.user.Usuario;
import org.example.GestorVideojuegosHibernateJavaFX.utils.DataProvider;
import org.example.GestorVideojuegosHibernateJavaFX.utils.JavaFXUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List; // Necesario para la función añadir()

public class MainController implements Initializable {

    // UI Elements
    @FXML
    private Label welcomeText;
    @FXML
    private Label lblUsuario;

    // Tabla y Columnas
    @FXML
    private TableView<Copia> tabla;
    @FXML
    private TableColumn<Copia, String> cId;
    @FXML
    private TableColumn<Copia, String> cTitulo;
    @FXML
    private TableColumn<Copia, String> cEstado;
    @FXML
    private TableColumn<Copia, String> cSoporte;

    // Botones de Usuario Estándar (CRUD Copia)
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnEditar;

    // Botón de Administrador
    @FXML
    private Button btnNuevaPelicula;

    // Servicios y Repositorios
    SimpleSessionService simpleSessionService = new SimpleSessionService();
    PeliculaRepository peliculaRepository = new PeliculaRepository(DataProvider.getSessionFactory());
    @FXML
    private Button btnCerrar;
    @FXML
    private Button btnSalir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Usuario activeUser = simpleSessionService.getActive();
        lblUsuario.setText("Copias de películas del usuario: " + activeUser.getNombreUsuario());

        // 1. Control de Acceso: Inicialmente, el botón es visible por FXML.
        // Ocultamos el botón si el usuario NO es administrador.
        if (activeUser.getIsAdmin() == null || !activeUser.getIsAdmin()) {
            btnNuevaPelicula.setVisible(false);
        }
        // Si es admin, el botón queda visible.

        // 2. Configurar CellValueFactory para las Copias
        cId.setCellValueFactory( (row)->{
            return new SimpleStringProperty(String.valueOf(row.getValue().getId()));
        });

        cTitulo.setCellValueFactory( (row)->{
            // Accedemos al título a través del objeto Pelicula asociado
            String title = row.getValue().getPelicula().getTitulo();
            return new SimpleStringProperty(title);
        });

        cEstado.setCellValueFactory( (row)->{
            return new SimpleStringProperty(row.getValue().getEstado());
        });

        cSoporte.setCellValueFactory( (row)->{
            return new SimpleStringProperty(row.getValue().getSoporte());
        });

        // 3. Listener para mostrar el detalle
        tabla.getSelectionModel().selectedItemProperty().addListener(showCopia());

        // 4. Cargar datos iniciales
        loadTableData();
    }

    // -----------------------------------------------------------------
    // METODOS DE NAVEGACIÓN Y UTILIDAD
    // -----------------------------------------------------------------

    /**
     * Cierra la sesión del usuario activo y regresa a la pantalla de Login.
     */
    @FXML
    public void cerrarSesion(ActionEvent actionEvent) {
        new SimpleSessionService().logout();
        JavaFXUtil.setScene("/org/example/GestorVideojuegosHibernateJavaFX/login-view.fxml");
    }

    /**
     * Cierra completamente la aplicación.
     */
    @FXML
    public void salirAplicacion(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Recarga los datos del usuario activo desde la BD y actualiza la tabla.
     */
    public void refresh() {
        try(Session s = DataProvider.getSessionFactory().openSession()) {
            // Recargar la entidad Usuario para actualizar su colección de Copias
            Usuario updatedUser = s.find(Usuario.class, simpleSessionService.getActive().getId());
            simpleSessionService.update(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadTableData();
    }

    private void loadTableData() {
        tabla.getItems().clear();
        // Usamos la colección EAGER cargada en la sesión
        simpleSessionService.getActive().getCopias().forEach(copia -> {
            tabla.getItems().add(copia);
        });
    }

    private ChangeListener<Copia> showCopia() {
        return (obs, old, news) -> {
            if (news != null) {
                JavaFXUtil.showModal(
                        Alert.AlertType.INFORMATION,
                        "Detalle de " + news.getPelicula().getTitulo(),
                        "Información de la Copia",
                        news.toString()
                );
            }
        };
    }

    // -----------------------------------------------------------------
    // METODOS CRUD (COPIA)
    // -----------------------------------------------------------------

    /**
     * Abre una ventana modal para modificar el estado y soporte de la copia seleccionada.
     */
    @FXML
    public void editar(ActionEvent actionEvent) {
        Copia selectedCopia = tabla.getSelectionModel().getSelectedItem();
        if (selectedCopia == null) {
            JavaFXUtil.showModal(Alert.AlertType.WARNING, "Advertencia", "Selección Requerida", "Por favor, selecciona una copia para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/GestorVideojuegosHibernateJavaFX/editar-copia-view.fxml"));
            Parent root = loader.load();

            EditarCopiaController controller = loader.getController();
            controller.setCopia(selectedCopia);

            Stage modalStage = new Stage();
            modalStage.setTitle("Modificar Copia - " + selectedCopia.getPelicula().getTitulo());
            modalStage.setScene(new Scene(root));
            modalStage.initOwner(JavaFXUtil.getStage());
            controller.setStage(modalStage);
            modalStage.initModality(Modality.WINDOW_MODAL);

            modalStage.showAndWait();

            refresh();

        } catch (IOException e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de edición.", e.getMessage());
        }
    }


    /**
     * Elimina la copia seleccionada de la base de datos.
     */
    @FXML
    public void borrar(ActionEvent actionEvent) {

        Copia selectedCopia = tabla.getSelectionModel().getSelectedItem();
        if(selectedCopia == null) return;

        try(Session s = DataProvider.getSessionFactory().openSession()) {
            s.beginTransaction();

            Usuario currentUser = s.find(Usuario.class, simpleSessionService.getActive().getId());
            Copia copiaToDelete = s.find(Copia.class, selectedCopia.getId());

            // Remover la copia de la colección del usuario antes de eliminarla de la BD
            currentUser.getCopias().removeIf(copia -> copia.getId().equals(selectedCopia.getId()));
            s.remove(copiaToDelete);

            s.getTransaction().commit();

            simpleSessionService.update(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error", "Error al borrar la copia", e.getMessage());
        }

        refresh();
    }

    /**
     * Abre una ventana modal con un selector de películas para añadir una nueva copia.
     */
    @FXML
    public void añadir(ActionEvent actionEvent) {
        try {
            // Cargar el FXML de la ventana modal de Añadir Copia
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/GestorVideojuegosHibernateJavaFX/añadir-copia-view.fxml"));
            Parent root = loader.load();

            AñadirCopiaController controller = loader.getController();

            // Crear una nueva Stage (Ventana) para el modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Añadir Nueva Copia");
            modalStage.setScene(new Scene(root));
            modalStage.initOwner(JavaFXUtil.getStage()); // Hace que el modal sea hijo de la ventana principal

            controller.setStage(modalStage); // Inyectar el Stage en el controlador modal

            modalStage.initModality(Modality.WINDOW_MODAL); // Bloquea la ventana principal

            modalStage.showAndWait(); // Mostrar la ventana y esperar a que se cierre

            refresh(); // Refrescar la tabla principal después de que el modal se cierre

        } catch (IOException e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de adición de copia.", e.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // METODO ADMIN
    // -----------------------------------------------------------------

    /**
     * Abre la ventana para que un Administrador pueda añadir una nueva película al catálogo.
     */
    @FXML
    public void añadirPeliculaAdmin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/GestorVideojuegosHibernateJavaFX/nueva-pelicula-view.fxml"));
            Parent root = loader.load();

            NuevaPeliculaController controller = loader.getController();

            Stage modalStage = new Stage();
            modalStage.setTitle("Añadir Nueva Película");
            modalStage.setScene(new Scene(root));
            modalStage.initOwner(JavaFXUtil.getStage());

            controller.setStage(modalStage);

            modalStage.initModality(Modality.WINDOW_MODAL);

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de Nueva Película.", e.getMessage());
        }
    }
}