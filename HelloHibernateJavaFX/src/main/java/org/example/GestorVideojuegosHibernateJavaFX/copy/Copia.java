// Contenido para src/main/java/.../copy/Copia.java
package org.example.GestorVideojuegosHibernateJavaFX.copy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.GestorVideojuegosHibernateJavaFX.movie.Pelicula;
import org.example.GestorVideojuegosHibernateJavaFX.user.Usuario;

import java.io.Serializable;

@Entity
@Table(name="copia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Copia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String estado;
    private String soporte; // DVD, Blu-ray, VHS

    // Relación Many-to-One: id_pelicula
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    // Relación Many-to-One: id_usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Override
    public String toString() {
        return pelicula.toString() + "\n" +
                "--- Detalle de Copia ---\n" +
                "ID Copia: " + id + "\n" +
                "Estado: " + estado + "\n" +
                "Soporte: " + soporte;
    }
}
