// Contenido para src/main/java/.../services/AuthService.java
package org.example.GestorVideojuegosHibernateJavaFX.services;

import org.example.GestorVideojuegosHibernateJavaFX.user.Usuario; // Cambio de User a Usuario
import org.example.GestorVideojuegosHibernateJavaFX.user.UsuarioRepository; // Cambio de UserRepository a UsuarioRepository

import java.util.Optional;

public class AuthService {

    UsuarioRepository usuarioRepository; // Cambio de UserRepository

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Adaptado a nombreUsuario y contraseña
    public Optional<Usuario> validateUser(String nombreUsuario, String contraseña) {
        Optional<Usuario> user = usuarioRepository.findByNombreUsuario(nombreUsuario); // Llama al nuevo método
        if (user.isPresent()) {
            if (user.get().getContraseña().equals(contraseña)) { // Cambio de getPassword a getContraseña
                return user;
            } else  {
                return Optional.empty();
            }
        }
        return user;
    }
}