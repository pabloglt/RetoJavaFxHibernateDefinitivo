package org.example.GestorVideojuegosHibernateJavaFX.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.GestorVideojuegosHibernateJavaFX.services.AuthService;
import org.example.GestorVideojuegosHibernateJavaFX.services.SimpleSessionService;
import org.example.GestorVideojuegosHibernateJavaFX.user.Usuario; // CAMBIO: Importamos Usuario
import org.example.GestorVideojuegosHibernateJavaFX.user.UsuarioRepository; // CAMBIO: Importamos UsuarioRepository
import org.example.GestorVideojuegosHibernateJavaFX.utils.DataProvider;
import org.example.GestorVideojuegosHibernateJavaFX.utils.JavaFXUtil;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @javafx.fxml.FXML
    private TextField txtContraseña;
    @javafx.fxml.FXML
    private TextField txtCorreo; // Usamos este TextField para el nombre_usuario
    @javafx.fxml.FXML
    private Label info;

    private UsuarioRepository usuarioRepository; // CAMBIO: Tipo de repositorio
    private AuthService authService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioRepository = new UsuarioRepository(DataProvider.getSessionFactory()); // CAMBIO: Instanciamos el nuevo repositorio
        authService = new AuthService(usuarioRepository);
    }

    @javafx.fxml.FXML
    public void entrar(ActionEvent actionEvent) {
        // Validación con nombre de usuario y contraseña
        Optional<Usuario> user = authService.validateUser(txtCorreo.getText(),txtContraseña.getText() );
        if (user.isPresent()){
            SimpleSessionService sessionService = new SimpleSessionService();
            sessionService.login(user.get());
            JavaFXUtil.setScene("/org/example/GestorVideojuegosHibernateJavaFX/main-view.fxml");
        }
    }

    @javafx.fxml.FXML
    public void Salir(ActionEvent actionEvent) {
        System.exit(0);
    }
}