package com.albumcollector.service;

import com.albumcollector.model.Lamina;
import com.albumcollector.repository.LaminaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaminaService {
    

    public List<Lamina> buscarLaminas(Long albumId, String estado, String tipoLamina) {
        final String tipoLaminaFinal = (tipoLamina != null && tipoLamina.isEmpty()) ? null : tipoLamina;
        final String estadoFinal = (estado != null && estado.isEmpty()) ? null : estado;

        
        if ("duplicada".equals(estadoFinal)) {
            if (albumId != null && tipoLaminaFinal != null) {
                return laminaRepository.findByAlbumId(albumId).stream()
                        .filter(l -> l.getCantidad() != null && l.getCantidad() > 1 && tipoLaminaFinal.equals(l.getTipoLamina()))
                        .toList();
            } else if (albumId != null) {
                return laminaRepository.findByAlbumId(albumId).stream()
                        .filter(l -> l.getCantidad() != null && l.getCantidad() > 1)
                        .toList();
            } else if (tipoLaminaFinal != null) {
                return laminaRepository.findAll().stream()
                        .filter(l -> l.getCantidad() != null && l.getCantidad() > 1 && tipoLaminaFinal.equals(l.getTipoLamina()))
                        .toList();
            } else {
                return laminaRepository.findAll().stream()
                        .filter(l -> l.getCantidad() != null && l.getCantidad() > 1)
                        .toList();
            }
        }

        
        if (albumId != null && estadoFinal != null && tipoLaminaFinal != null) {
            return laminaRepository.findByAlbumIdAndEstadoAndTipoLamina(albumId, estadoFinal, tipoLaminaFinal);
        } else if (albumId != null && estadoFinal != null) {
            return laminaRepository.findByAlbumIdAndEstado(albumId, estadoFinal);
        } else if (albumId != null && tipoLaminaFinal != null) {
            return laminaRepository.findByAlbumId(albumId).stream()
                    .filter(l -> tipoLaminaFinal.equals(l.getTipoLamina()))
                    .toList();
        } else if (estadoFinal != null && tipoLaminaFinal != null) {
            return laminaRepository.findByEstadoAndTipoLamina(estadoFinal, tipoLaminaFinal);
        } else if (albumId != null) {
            return laminaRepository.findByAlbumId(albumId);
        } else if (estadoFinal != null) {
            return laminaRepository.findByEstado(estadoFinal);
        } else if (tipoLaminaFinal != null) {
            return laminaRepository.findAll().stream()
                    .filter(l -> tipoLaminaFinal.equals(l.getTipoLamina()))
                    .toList();
        } else {
            return laminaRepository.findAll();
        }
    }
    public List<Lamina> getFaltantes(Long albumId) {
        return getFaltantesByTipo(albumId, null);
    }

    public List<Lamina> getRepetidas(Long albumId) {
        return getRepetidasByTipo(albumId, null);
    }
        public List<Lamina> getNormales(String tipoLamina) {
            if (tipoLamina == null || tipoLamina.isEmpty()) {
                return laminaRepository.findByEstado("normal");
            } else {
                return laminaRepository.findByEstadoAndTipoLamina("normal", tipoLamina);
            }
        }
    @Autowired
    private LaminaRepository laminaRepository;

    public List<Lamina> getAllLaminas() {
        return laminaRepository.findAll();
    }

    public Optional<Lamina> getLaminaById(Long id) {
        return laminaRepository.findById(id);
    }

    public Lamina saveLamina(Lamina lamina) {
        
        Optional<Lamina> existente = laminaRepository.findByNombreAndAlbumId(lamina.getNombre(), lamina.getAlbum().getId());
        if (existente.isPresent()) {
            Lamina laminaExistente = existente.get();
           
            int nuevaCantidad = (laminaExistente.getCantidad() != null ? laminaExistente.getCantidad() : 0)
                + (lamina.getCantidad() != null ? lamina.getCantidad() : 1);
            laminaExistente.setCantidad(nuevaCantidad);
           
            laminaExistente.setEstado(lamina.getEstado());
            laminaExistente.setImagen(lamina.getImagen());
            laminaExistente.setTipoLamina(lamina.getTipoLamina());
            return laminaRepository.save(laminaExistente);
        } else {
            
            return laminaRepository.save(lamina);
        }
    }

    public List<Lamina> saveAllLaminas(List<Lamina> laminas) {
        return laminaRepository.saveAll(laminas);
    }

    public Lamina updateLamina(Long id, Lamina laminaDetails) {
        Optional<Lamina> optionalLamina = laminaRepository.findById(id);
        if (optionalLamina.isPresent()) {
            Lamina lamina = optionalLamina.get();
            String estadoOriginal = lamina.getEstado();
            String estadoNuevo = laminaDetails.getEstado();
            lamina.setNombre(laminaDetails.getNombre());
            lamina.setImagen(laminaDetails.getImagen());
            lamina.setEstado(estadoNuevo);
            if ("no_obtenida".equals(estadoOriginal) && "obtenida".equals(estadoNuevo)) {
                lamina.setCantidad(1);
            } else if ("obtenida".equals(estadoOriginal) && "no_obtenida".equals(estadoNuevo)) {
                lamina.setCantidad(0);
            } else if ("duplicada".equals(estadoNuevo)) {
                lamina.setCantidad(laminaDetails.getCantidad());
            } else if ("obtenida".equals(estadoNuevo)) {
                // Si es obtenida, la cantidad debe ser 1
                lamina.setCantidad(1);
            } else if ("no_obtenida".equals(estadoNuevo)) {
                lamina.setCantidad(0);
            } else {
                lamina.setCantidad(null);
            }
            lamina.setTipoLamina(laminaDetails.getTipoLamina());
            return laminaRepository.save(lamina);
        }
        return null;
    }

    public void deleteLamina(Long id) {
        laminaRepository.deleteById(id);
    }


    public List<Lamina> getFaltantesByTipo(Long albumId, String tipoLamina) {
        if (albumId == null) {
            if (tipoLamina == null || tipoLamina.isEmpty()) {
                return laminaRepository.findByEstado("faltante");
            } else {
                return laminaRepository.findByEstadoAndTipoLamina("faltante", tipoLamina);
            }
        } else {
            if (tipoLamina == null || tipoLamina.isEmpty()) {
                return laminaRepository.findByAlbumIdAndEstado(albumId, "faltante");
            } else {
                return laminaRepository.findByAlbumIdAndEstadoAndTipoLamina(albumId, "faltante", tipoLamina);
            }
        }
    }

    public List<Lamina> getRepetidasByTipo(Long albumId, String tipoLamina) {
        if (albumId == null) {
            if (tipoLamina == null || tipoLamina.isEmpty()) {
                return laminaRepository.findByEstado("repetida");
            } else {
                return laminaRepository.findByEstadoAndTipoLamina("repetida", tipoLamina);
            }
        } else {
            if (tipoLamina == null || tipoLamina.isEmpty()) {
                return laminaRepository.findByAlbumIdAndEstado(albumId, "repetida");
            } else {
                return laminaRepository.findByAlbumIdAndEstadoAndTipoLamina(albumId, "repetida", tipoLamina);
            }
        }
    }
}
