// Contenido para src/main/java/.../user/Usuario.java
package org.example.GestorVideojuegosHibernateJavaFX.user;

import jakarta.persistence.*;
import lombok.Data;
import org.example.GestorVideojuegosHibernateJavaFX.copy.Copia; // Importar la nueva clase Copia

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="usuario")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nombre_usuario") // Campo nombre_usuario para el login
    private String nombreUsuario;

    private String contraseña; // Cambiamos a 'contraseña'

    @Column(name="is_admin")
    private Boolean isAdmin;

    // Relación One-to-Many: Colección de Copias (NO de Videojuegos)
    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Copia> copias = new ArrayList<>();

    // Método para añadir Copia
    public void addCopia(Copia c){
        c.setUsuario(this);
        this.copias.add(c);
    }
}