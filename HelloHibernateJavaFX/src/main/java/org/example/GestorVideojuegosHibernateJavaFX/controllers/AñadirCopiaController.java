package org.example.GestorVideojuegosHibernateJavaFX.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.GestorVideojuegosHibernateJavaFX.copy.Copia;
import org.example.GestorVideojuegosHibernateJavaFX.movie.Pelicula;
import org.example.GestorVideojuegosHibernateJavaFX.movie.PeliculaRepository;
import org.example.GestorVideojuegosHibernateJavaFX.services.SimpleSessionService;
import org.example.GestorVideojuegosHibernateJavaFX.utils.DataProvider;
import org.example.GestorVideojuegosHibernateJavaFX.utils.JavaFXUtil;
import org.hibernate.Session;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AñadirCopiaController implements Initializable {

    @FXML
    private ComboBox<Pelicula> cmbPelicula;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtSoporte;

    private Stage stage;
    private PeliculaRepository peliculaRepository = new PeliculaRepository(DataProvider.getSessionFactory());
    private SimpleSessionService sessionService = new SimpleSessionService();
    @FXML
    private Button btnCrearCopia;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cargar todas las Peliculas en el ComboBox
        List<Pelicula> peliculas = peliculaRepository.findAll();
        cmbPelicula.setItems(FXCollections.observableArrayList(peliculas));

        // 2. Definir cómo se muestra el objeto Pelicula en el ComboBox
        cmbPelicula.setCellFactory(lv -> new ListCell<Pelicula>() {
            @Override
            protected void updateItem(Pelicula item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.getTitulo());
            }
        });
        cmbPelicula.setButtonCell(new ListCell<Pelicula>() {
            @Override
            protected void updateItem(Pelicula item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.getTitulo());
            }
        });
    }

    // Método llamado por MainController para inyectar la ventana Stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void guardar(ActionEvent actionEvent) {
        Pelicula peliculaSeleccionada = cmbPelicula.getSelectionModel().getSelectedItem();

        if (peliculaSeleccionada == null) {
            JavaFXUtil.showModal(Alert.AlertType.WARNING, "Error de Selección", "Película Requerida", "Por favor, selecciona una película del listado.");
            return;
        }

        Copia newCopia = new Copia();
        newCopia.setEstado(txtEstado.getText());
        newCopia.setSoporte(txtSoporte.getText());
        newCopia.setPelicula(peliculaSeleccionada);

        sessionService.getActive().addCopia(newCopia);

        try (Session s = DataProvider.getSessionFactory().openSession()) {
            s.beginTransaction();
            s.merge(sessionService.getActive());
            s.getTransaction().commit();

            sessionService.update(s.find(sessionService.getActive().getClass(), sessionService.getActive().getId()));

            JavaFXUtil.showModal(Alert.AlertType.INFORMATION, "Éxito", "Copia Creada", "La copia de " + peliculaSeleccionada.getTitulo() + " ha sido agregada.");

            // CERRAR LA VENTANA MODAL
            if (stage != null) {
                stage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error de BD", "No se pudo guardar la copia", e.getMessage());
        }
    }
}