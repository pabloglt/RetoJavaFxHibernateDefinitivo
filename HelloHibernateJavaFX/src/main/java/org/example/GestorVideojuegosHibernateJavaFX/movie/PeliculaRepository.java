// Contenido para src/main/java/.../movie/PeliculaRepository.java
package org.example.GestorVideojuegosHibernateJavaFX.movie; // Cambiado de game a movie

import org.example.GestorVideojuegosHibernateJavaFX.utils.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

// Repositorio para la entidad Pelicula
public class PeliculaRepository implements Repository<Pelicula> { // Cambio de Game a Pelicula

    SessionFactory sessionFactory;

    public PeliculaRepository(SessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
    }

    @Override
    public Pelicula save(Pelicula entity) {
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    // El resto de métodos no tienen sentido para Pelicula en este reto,
    // ya que solo el administrador puede manipularlas (excepto el save que es útil)

    @Override
    public Optional<Pelicula> delete(Pelicula entity) { return Optional.empty(); }
    @Override
    public Optional<Pelicula> deleteById(Long id) { return Optional.empty(); }

    @Override
    public Optional<Pelicula> findById(Long id) {
        try(Session session=sessionFactory.openSession()){
            return Optional.ofNullable(session.find(Pelicula.class, id));
        }
    }

    @Override
    public List<Pelicula> findAll() {
        try(Session session=sessionFactory.openSession()){
            return session.createQuery("from Pelicula",Pelicula.class).list(); // Consulta adaptada
        }
    }

    @Override
    public Long count() {
        try(Session session=sessionFactory.openSession()){
            return session.createQuery("SELECT count(p) from Pelicula p",Long.class).getSingleResult();
        }
    }
}
