package com.albumcollector.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.albumcollector.model.Album;
import com.albumcollector.model.Lamina;
import com.albumcollector.repository.AlbumRepository;
import com.albumcollector.repository.LaminaRepository;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private LaminaRepository laminaRepository;

    @Override
    public void run(String... args) {
        if (albumRepository.count() == 0) {
            Album album = new Album();
            album.setNombre("Pokémon");
            album.setImagen("/uploads/img/pokemon_album.jpg");
            album.setFechaLanzamiento(LocalDate.of(2020, 1, 1));
            album.setCantidadLaminas(150);
            album = albumRepository.save(album);

            String[] tipos = {"Rara", "Poco común", "Común"};
            Lamina[] laminas = new Lamina[150];
            for (int i = 0; i < 150; i++) {
                Lamina lamina = new Lamina();
                lamina.setNombre("Pokemon #" + (i+1));
                lamina.setImagen("/uploads/img/pokemon_" + (i+1) + ".jpg");
                lamina.setTipoLamina(tipos[i % tipos.length]);
                lamina.setAlbum(album);
                if (i < 30) {
                    lamina.setEstado("obtenida");
                    lamina.setCantidad(1);
                } else if (i < 40) {
                    lamina.setEstado("obtenida");
                    lamina.setCantidad(2 + (i % 3)); 
                } else {
                    lamina.setEstado("no_obtenida");
                    lamina.setCantidad(0);
                }
                laminas[i] = lamina;
            }
            laminaRepository.saveAll(Arrays.asList(laminas));
        }
    }
}
