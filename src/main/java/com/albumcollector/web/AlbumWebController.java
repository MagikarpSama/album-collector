package com.albumcollector.web;

import com.albumcollector.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.albumcollector.model.Album;


@Controller
@RequestMapping("/albums")
public class AlbumWebController {
    @Autowired
    private AlbumService albumService;

    @GetMapping
    public String listAlbums(Model model) {
        var albums = albumService.getAllAlbums();
        Map<Long, Integer> repetidasPorAlbum = new HashMap<>();
        Map<Long, Integer> cantidadLaminasPorAlbum = new HashMap<>();
        for (var album : albums) {
            int repetidas = 0;
            int cantidadLaminasObtenidas = 0;
            if (album.getLaminas() != null) {
                for (var lamina : album.getLaminas()) {
                    if (lamina.getCantidad() != null && lamina.getEstado() != null && !"no_obtenida".equals(lamina.getEstado())) {
                        cantidadLaminasObtenidas++;
                        if (lamina.getCantidad() > 1) {
                            repetidas += lamina.getCantidad() - 1;
                        }
                    }
                }
            }
            repetidasPorAlbum.put(album.getId(), repetidas);
            cantidadLaminasPorAlbum.put(album.getId(), cantidadLaminasObtenidas);
        }
        model.addAttribute("albums", albums);
        model.addAttribute("repetidasPorAlbum", repetidasPorAlbum);
        model.addAttribute("cantidadLaminasPorAlbum", cantidadLaminasPorAlbum);
        return "albums";
    }

    @GetMapping("/nuevo")
    public String showAlbumForm(Model model) {
        model.addAttribute("album", new Album());
        return "album_form";
    }

    @PostMapping("/nuevo")
    public String saveAlbum(@Valid @ModelAttribute Album album, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "album_form";
        }
        albumService.saveAlbum(album);
        return "redirect:/albums";
    }

    @GetMapping("/editar/{id}")
    public String showEditAlbumForm(@PathVariable Long id, Model model) {
        Album album = albumService.getAlbumById(id).orElse(null);
        if (album == null) return "redirect:/albums";
        model.addAttribute("album", album);
        return "album_form";
    }

    @PostMapping("/editar/{id}")
    public String updateAlbum(@PathVariable Long id, @Valid @ModelAttribute Album album, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "album_form";
        }
        albumService.updateAlbum(id, album);
        return "redirect:/albums";
    }

    @GetMapping("/eliminar/{id}")
    public String deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return "redirect:/albums";
    }
}
