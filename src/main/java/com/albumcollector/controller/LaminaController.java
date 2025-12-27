package com.albumcollector.controller;

import com.albumcollector.model.Lamina;
import com.albumcollector.service.LaminaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/laminas")
public class LaminaController {
    @Autowired
    private LaminaService laminaService;

    @GetMapping
    public List<Lamina> getAllLaminas() {
        return laminaService.getAllLaminas();
    }

    @GetMapping("/{id}")
    public Lamina getLaminaById(@PathVariable Long id) {
        return laminaService.getLaminaById(id).orElse(null);
    }

    @PostMapping
    public Lamina createLamina(@RequestBody Lamina lamina) {
        return laminaService.saveLamina(lamina);
    }

    @PutMapping("/{id}")
    public Lamina updateLamina(@PathVariable Long id, @RequestBody Lamina laminaDetails) {
        return laminaService.updateLamina(id, laminaDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteLamina(@PathVariable Long id) {
        laminaService.deleteLamina(id);
    }

    //subir imagen
    @PostMapping("/{id}/imagen")
    public Lamina uploadImagen(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        
        Lamina lamina = laminaService.getLaminaById(id).orElse(null);
        if (lamina != null && !file.isEmpty()) {
            lamina.setImagen(file.getOriginalFilename());
            return laminaService.saveLamina(lamina);
        }
        return null;
    }

    // carga de laminas
    @PostMapping("/bulk")
    public List<Lamina> bulkCreateLaminas(@RequestBody List<Lamina> laminas) {
        return laminaService.saveAllLaminas(laminas);
    }

    
    @GetMapping("/faltantes/{albumId}")
    public List<Lamina> getFaltantes(@PathVariable Long albumId) {
        return laminaService.getFaltantes(albumId);
    }

    
    @GetMapping("/repetidas/{albumId}")
    public List<Lamina> getRepetidas(@PathVariable Long albumId) {
        return laminaService.getRepetidas(albumId);
    }
}
