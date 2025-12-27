package com.albumcollector.service;

import com.albumcollector.model.Album;
import com.albumcollector.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }

    public Album updateAlbum(Long id, Album albumDetails) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            album.setNombre(albumDetails.getNombre());
            album.setImagen(albumDetails.getImagen());
            album.setFechaLanzamiento(albumDetails.getFechaLanzamiento());
            album.setCantidadLaminas(albumDetails.getCantidadLaminas());
            return albumRepository.save(album);
        }
        return null;
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}
