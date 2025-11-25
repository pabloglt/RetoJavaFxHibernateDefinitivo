package org.example.GestorVideojuegosHibernateJavaFX.services;

import org.example.GestorVideojuegosHibernateJavaFX.user.Usuario; // CAMBIO: Importamos Usuario

import java.util.HashMap;

public class SimpleSessionService implements SessionService<Usuario> { // CAMBIO: Tipo genérico a Usuario

    private static Usuario activeUser = null; // CAMBIO: De User a Usuario
    private static HashMap<String,Object> data = new HashMap<String,Object>();

    public void login(Usuario user) { // CAMBIO: Tipo de parámetro a Usuario
        activeUser = user;
    }

    public void update(Usuario user) { // Añadimos el update para recargar el usuario y sus copias
        activeUser = user;
    }

    public boolean isLoggedIn(){
        return activeUser != null;
    }

    public void logout() {
        activeUser = null;
        data.clear();
    }

    @Override
    public Usuario getActive() { return activeUser; } // CAMBIO: Retorna Usuario

    @Override
    public void setObject(String key, Object o) { data.put(key,o); }

    @Override
    public Object getObject(String key) { return data.get(key); }
}