// Contenido para src/main/java/.../user/UsuarioRepository.java
package org.example.GestorVideojuegosHibernateJavaFX.user;

import org.example.GestorVideojuegosHibernateJavaFX.utils.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements Repository<Usuario> { // Cambio de User a Usuario

    private SessionFactory sessionFactory;

    public UsuarioRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Adaptamos este método para buscar por nombreUsuario y no por email
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        try(Session session = sessionFactory.openSession()) {
            Query<Usuario> q = session.createQuery(
                    "from Usuario where nombreUsuario=:nombreUsuario",Usuario.class);
            q.setParameter("nombreUsuario", nombreUsuario);
            return Optional.ofNullable(q.uniqueResult());
        }
    }

    // Mantenemos los stubs (cuerpos vacíos) de los otros métodos por ahora
    @Override
    public Usuario save(Usuario entity) { return null; }
    @Override
    public Optional<Usuario> delete(Usuario entity) { return Optional.empty(); }
    @Override
    public Optional<Usuario> deleteById(Long id) { return Optional.empty(); }
    @Override
    public Optional<Usuario> findById(Long id) { return Optional.empty(); }
    @Override
    public List<Usuario> findAll() { return List.of(); }
    @Override
    public Long count() { return 0L; }
}