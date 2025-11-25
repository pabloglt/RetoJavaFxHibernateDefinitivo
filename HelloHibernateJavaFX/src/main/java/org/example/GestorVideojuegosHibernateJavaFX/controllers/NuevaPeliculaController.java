package org.example.GestorVideojuegosHibernateJavaFX.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.GestorVideojuegosHibernateJavaFX.movie.Pelicula;
import org.example.GestorVideojuegosHibernateJavaFX.movie.PeliculaRepository;
import org.example.GestorVideojuegosHibernateJavaFX.utils.DataProvider;
import org.example.GestorVideojuegosHibernateJavaFX.utils.JavaFXUtil;

/**
 * Controlador para la ventana modal de adición de Películas (solo Admin).
 */
public class NuevaPeliculaController {

    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtGenero;
    @FXML
    private TextField txtAño;
    @FXML
    private TextField txtDirector;
    @FXML
    private TextArea txtDescripcion;

    private Stage stage;
    private PeliculaRepository peliculaRepository = new PeliculaRepository(DataProvider.getSessionFactory());
    @FXML
    private Button btnGuardar;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void guardar(ActionEvent actionEvent) {
        try {
            // 1. Crear el objeto Pelicula
            Pelicula nuevaPelicula = new Pelicula();
            nuevaPelicula.setTitulo(txtTitulo.getText());
            nuevaPelicula.setGenero(txtGenero.getText());
            nuevaPelicula.setDirector(txtDirector.getText());
            nuevaPelicula.setDescripcion(txtDescripcion.getText());

            // Convertir el año (manejar errores)
            try {
                nuevaPelicula.setAño(Integer.parseInt(txtAño.getText()));
            } catch (NumberFormatException e) {
                JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error", "Dato Inválido", "El campo Año debe ser un número entero.");
                return;
            }

            // 2. Persistir en la base de datos
            peliculaRepository.save(nuevaPelicula);

            JavaFXUtil.showModal(Alert.AlertType.INFORMATION, "Éxito", "Película Añadida", "La película ha sido agregada al catálogo.");

            // 3. Cerrar la ventana modal
            if (stage != null) {
                stage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error de BD", "No se pudo guardar la película", e.getMessage());
        }
    }
}
