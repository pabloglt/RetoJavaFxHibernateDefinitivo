// Contenido para src/main/java/.../movie/Pelicula.java
package org.example.GestorVideojuegosHibernateJavaFX.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="pelicula")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pelicula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String genero;
    private Integer año;
    private String descripcion;
    private String director;

    // El toString es útil para el modal de detalle
    @Override
    public String toString() {
        return "Película: " + titulo + " (" + año + ")\n" +
                "Género: " + genero + "\n" +
                "Director: " + director;
    }
}