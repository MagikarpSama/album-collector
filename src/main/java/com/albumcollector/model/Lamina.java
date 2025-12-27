package com.albumcollector.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Lamina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "La imagen es obligatoria")
    private String imagen;

    @NotBlank(message = "El estado es obligatorio")
    private String estado; 

    @Min(value = 0, message = "No puede ser negativo")
    private Integer cantidad;


    @NotBlank(message = "El tipo de lámina es obligatorio")
    private String tipoLamina; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @NotNull(message = "El álbum es obligatorio")
    private Album album;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getTipoLamina() { return tipoLamina; }
    public void setTipoLamina(String tipoLamina) { this.tipoLamina = tipoLamina; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
}