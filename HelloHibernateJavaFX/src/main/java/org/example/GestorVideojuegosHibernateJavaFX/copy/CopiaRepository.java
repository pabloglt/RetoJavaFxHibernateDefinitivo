// Contenido para src/main/java/.../copy/CopiaRepository.java
package org.example.GestorVideojuegosHibernateJavaFX.copy;

import org.example.GestorVideojuegosHibernateJavaFX.utils.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class CopiaRepository implements Repository<Copia> { // Implementa Repository para Copia

    SessionFactory sessionFactory;

    public CopiaRepository(SessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
    }

    @Override
    public Copia save(Copia entity) {
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    @Override
    public Optional<Copia> delete(Copia entity) {
        try(Session session=sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<Copia> deleteById(Long id) {
        try(Session session=sessionFactory.openSession()){
            Copia copia = session.find(Copia.class,id);
            if(copia!=null){
                session.beginTransaction();
                session.remove(copia);
                session.getTransaction().commit();
            }
            return Optional.ofNullable(copia);
        }
    }

    @Override
    public Optional<Copia> findById(Long id) {
        try(Session session=sessionFactory.openSession()){
            return Optional.ofNullable(session.find(Copia.class, id));
        }
    }

    @Override
    public List<Copia> findAll() {
        try(Session session=sessionFactory.openSession()){
            return session.createQuery("from Copia",Copia.class).list();
        }
    }

    @Override
    public Long count() {
        try(Session session=sessionFactory.openSession()){
            Long salida = session.createQuery("SELECT count(c) from Copia c",Long.class).getSingleResult();
            return salida;
        }
    }
}
