
package com.albumcollector.repository;

import com.albumcollector.model.Lamina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LaminaRepository extends JpaRepository<Lamina, Long> {
    List<Lamina> findByAlbumId(Long albumId);
    List<Lamina> findByEstado(String estado);
    List<Lamina> findByEstadoAndTipoLamina(String estado, String tipoLamina);
    List<Lamina> findByAlbumIdAndEstado(Long albumId, String estado);
    List<Lamina> findByAlbumIdAndEstadoAndTipoLamina(Long albumId, String estado, String tipoLamina);
    Optional<Lamina> findByNombreAndAlbumId(String nombre, Long albumId);
}