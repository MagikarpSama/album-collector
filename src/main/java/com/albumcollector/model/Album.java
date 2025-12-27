package com.albumcollector.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "La imagen es obligatoria")
    private String imagen;

    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    private LocalDate fechaLanzamiento;

    @NotNull(message = "La cantidad de láminas es obligatoria")
    private Integer cantidadLaminas;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lamina> laminas;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public Integer getCantidadLaminas() { return cantidadLaminas; }
    public void setCantidadLaminas(Integer cantidadLaminas) { this.cantidadLaminas = cantidadLaminas; }

    public List<Lamina> getLaminas() { return laminas; }
    public void setLaminas(List<Lamina> laminas) { this.laminas = laminas; }
}