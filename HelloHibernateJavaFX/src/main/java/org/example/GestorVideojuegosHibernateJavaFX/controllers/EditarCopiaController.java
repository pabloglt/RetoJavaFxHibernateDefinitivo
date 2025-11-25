package org.example.GestorVideojuegosHibernateJavaFX.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.GestorVideojuegosHibernateJavaFX.copy.Copia;
import org.example.GestorVideojuegosHibernateJavaFX.utils.DataProvider;
import org.example.GestorVideojuegosHibernateJavaFX.utils.JavaFXUtil;
import org.hibernate.Session;
import javafx.scene.control.Alert;

/**
 * Controlador para la ventana modal de edición de Copia.
 */
public class EditarCopiaController {

    @FXML
    private Label lblTituloPelicula;
    @FXML
    private Label lblIdCopia;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtSoporte;

    private Copia copiaActual;
    private Stage stage;
    @FXML
    private Button btnGuardar;

    // Se llama desde el MainController para inyectar la copia y el Stage
    public void setCopia(Copia copia) {
        this.copiaActual = copia;
        lblIdCopia.setText(String.valueOf(copia.getId()));
        lblTituloPelicula.setText("Película: " + copia.getPelicula().getTitulo());
        txtEstado.setText(copia.getEstado());
        txtSoporte.setText(copia.getSoporte());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void guardar(ActionEvent actionEvent) {
        if (copiaActual == null) return;

        // 1. Actualizar el objeto Copia con los datos de los campos
        copiaActual.setEstado(txtEstado.getText());
        copiaActual.setSoporte(txtSoporte.getText());

        // 2. Persistir los cambios en la base de datos
        try (Session s = DataProvider.getSessionFactory().openSession()) {
            s.beginTransaction();
            // Utilizamos merge para guardar la entidad modificada
            s.merge(copiaActual);
            s.getTransaction().commit();

            JavaFXUtil.showModal(Alert.AlertType.INFORMATION, "Éxito", "Copia modificada", "Los datos se han guardado correctamente.");

            // 3. Cerrar la ventana modal
            if (stage != null) {
                stage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JavaFXUtil.showModal(Alert.AlertType.ERROR, "Error de BD", "No se pudo guardar la copia", e.getMessage());
        }
    }
}
