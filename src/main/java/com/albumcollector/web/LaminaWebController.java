package com.albumcollector.web;

import com.albumcollector.service.LaminaService;
import com.albumcollector.service.AlbumService;
import com.albumcollector.model.Lamina;
import com.albumcollector.model.Album;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/laminas")
public class LaminaWebController {
    @Autowired
    private LaminaService laminaService;
    @Autowired
    private AlbumService albumService;

    @GetMapping("")
    public String listLaminas(
            @RequestParam(value = "albumId", required = false) Long albumId,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "tipoLamina", required = false) String tipoLamina,
            @RequestParam(value = "buscar", required = false) String buscar,
            Model model) {
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("lamina", new Lamina());
        model.addAttribute("albumId", albumId);
        model.addAttribute("estado", estado);
        model.addAttribute("tipoLamina", tipoLamina);
        boolean showResults = buscar != null;
        model.addAttribute("showResults", showResults);
        model.addAttribute("laminas", showResults ? laminaService.buscarLaminas(albumId, estado, tipoLamina) : null);
        return "laminas";
    }

    @GetMapping("/nueva")
    public String showLaminaForm(Model model) {
        model.addAttribute("lamina", new Lamina());
        model.addAttribute("albums", albumService.getAllAlbums());
        return "lamina_form";
    }

    @PostMapping("/nueva")
    public String saveLamina(@Valid @ModelAttribute Lamina lamina, BindingResult result, Model model, @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile, @RequestParam(value = "tipoLamina", required = false) String tipoLamina, RedirectAttributes redirectAttributes) throws IOException {
        model.addAttribute("normales", laminaService.getNormales(tipoLamina));
        model.addAttribute("faltantes", laminaService.getFaltantesByTipo(null, tipoLamina));
        model.addAttribute("repetidas", laminaService.getRepetidasByTipo(null, tipoLamina));
        model.addAttribute("tipoLamina", tipoLamina);
        model.addAttribute("albums", albumService.getAllAlbums());
        // Validación condicional para cantidad
        if ("duplicada".equals(lamina.getEstado())) {
            if (lamina.getCantidad() == null || lamina.getCantidad() <= 0) {
                lamina.setCantidad(1); // Forzar mínimo 1
            }
        } else {
            lamina.setCantidad(null);
        }
        // Validación de tipo de archivo imagen
        if (imagenFile != null && !imagenFile.isEmpty()) {
            String contentType = imagenFile.getContentType();
            if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/webp"))) {
                result.rejectValue("imagen", "InvalidType", "Solo se permiten imágenes JPG, JPEG, PNG o WEBP");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("showModal", true);
            return "laminas";
        }
        if (imagenFile != null && !imagenFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "img" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String filePath = uploadDir + imagenFile.getOriginalFilename();
            imagenFile.transferTo(new File(filePath));
            lamina.setImagen("/uploads/img/" + imagenFile.getOriginalFilename());
        }
        laminaService.saveLamina(lamina);
        redirectAttributes.addFlashAttribute("successMessage", "¡Lámina creada exitosamente!");
        return "redirect:/laminas";
    }


    @GetMapping("/editar/{id}")
    public String showEditLaminaForm(@PathVariable Long id, @RequestParam(value = "tipoLamina", required = false) String tipoLamina, Model model) {
        Lamina lamina = laminaService.getLaminaById(id).orElse(null);
        if (lamina == null) return "redirect:/laminas";
        model.addAttribute("normales", laminaService.getNormales(tipoLamina));
        model.addAttribute("faltantes", laminaService.getFaltantesByTipo(null, tipoLamina));
        model.addAttribute("repetidas", laminaService.getRepetidasByTipo(null, tipoLamina));
        model.addAttribute("tipoLamina", tipoLamina);
        model.addAttribute("lamina", new Lamina());
        model.addAttribute("editLamina", lamina);
        model.addAttribute("albums", albumService.getAllAlbums());
        model.addAttribute("showEditModal", true);
        return "laminas";
    }

    @PostMapping("/editar/{id}")
    public String updateLamina(
        @PathVariable Long id,
        @Valid @ModelAttribute("editLamina") Lamina lamina,
        BindingResult result,
        Model model,
        @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
        @RequestParam(value = "albumId", required = false) Long albumId,
        @RequestParam(value = "estado", required = false) String estado,
        @RequestParam(value = "tipoLamina", required = false) String tipoLamina
    ) throws IOException {
        model.addAttribute("normales", laminaService.getNormales(tipoLamina));
        model.addAttribute("faltantes", laminaService.getFaltantesByTipo(null, tipoLamina));
        model.addAttribute("repetidas", laminaService.getRepetidasByTipo(null, tipoLamina));
        model.addAttribute("tipoLamina", tipoLamina);
        model.addAttribute("lamina", new Lamina());
        model.addAttribute("albums", albumService.getAllAlbums());

        if (result.hasErrors()) {
            model.addAttribute("editLamina", lamina);
            model.addAttribute("showEditModal", true);
            return "laminas";
        }
        if (imagenFile != null && !imagenFile.isEmpty()) {
            String uploadDir = "src/main/resources/static/img/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String filePath = uploadDir + imagenFile.getOriginalFilename();
            imagenFile.transferTo(new File(filePath));
            lamina.setImagen("/img/" + imagenFile.getOriginalFilename());
        }
        laminaService.updateLamina(id, lamina);
        String redirectUrl = String.format("redirect:/laminas?albumId=%s&estado=%s&tipoLamina=%s", albumId != null ? albumId : "", estado != null ? estado : "", tipoLamina != null ? tipoLamina : "");
        return redirectUrl;
    }

    @GetMapping("/eliminar/{id}")
    public String deleteLamina(@PathVariable Long id,
        @RequestParam(value = "albumId", required = false) Long albumId,
        @RequestParam(value = "estado", required = false) String estado,
        @RequestParam(value = "tipoLamina", required = false) String tipoLamina) {
        laminaService.deleteLamina(id);
        StringBuilder redirectUrl = new StringBuilder("redirect:/laminas?");
        if (albumId != null) redirectUrl.append("albumId=" + albumId + "&");
        if (estado != null && !estado.isEmpty()) redirectUrl.append("estado=" + estado + "&");
        if (tipoLamina != null && !tipoLamina.isEmpty()) redirectUrl.append("tipoLamina=" + tipoLamina + "&");
        redirectUrl.append("buscar=1");
        return redirectUrl.toString();
    }

    // Eliminadas rutas innecesarias
}
