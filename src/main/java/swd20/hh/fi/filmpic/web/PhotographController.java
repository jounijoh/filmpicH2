package swd20.hh.fi.filmpic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import swd20.hh.fi.filmpic.domain.CameraRepository;
import swd20.hh.fi.filmpic.domain.FilmRepository;
import swd20.hh.fi.filmpic.domain.Photograph;
import swd20.hh.fi.filmpic.domain.PhotographRepository;
import swd20.hh.fi.filmpic.domain.PhotographService;

@Controller
public class PhotographController {

	@Autowired
	private PhotographRepository photorepo;
	@Autowired
	private FilmRepository filmrepo;
	@Autowired
	private CameraRepository camerarepo;
	@Autowired
	private PhotographService photoservice;
	
	
	// List all photographs
	@GetMapping("/allphotos")
	public String getPhotolist(Model model, @Param("keyword") String keyword) {
		List<Photograph> listPhotos = photoservice.listAll(keyword); 
		model.addAttribute("photos", listPhotos);
        model.addAttribute("keyword", keyword);
		return "photolist";
	}
	
	// Add new phtograph
	@GetMapping("/addphoto")
	public String addPhoto(Model model) {
		model.addAttribute("photo", new Photograph());
		model.addAttribute("films", filmrepo.findAll());
		model.addAttribute("cameras", camerarepo.findAll());
		
		return "addphoto";
	}
	
	// Save photogrpah
	@PostMapping("savephoto")
	public String savePhoto(Photograph photo) {
		String filename = photo.getFilePath();
		photo.setFilePath("/images/" + filename + ".jpg");
		photorepo.save(photo);
		
		return "redirect:/allphotos";
	}
	// Delete photograph
	@GetMapping("/deletephoto/{id}")
	public String deleteBook(@PathVariable(name = "id") Long id) {

		photorepo.deleteById(id); // SQL DELETE
		return "redirect:/allphotos"; // uudelleenohjaus listaussivulle
	}
	// Edit photograph
	
	@GetMapping("editphoto/{id}")
	public String editPhotograph(@PathVariable(name = "id") Long id, Model model) {

		Photograph photo = photorepo.findById(id) // Haetaan kuva tietokannasta id:n perusteella
				.orElseThrow(() -> new IllegalArgumentException("Invalid photo Id:" + id));
		model.addAttribute("photo", photo); // kuvan tiedot -> templatelle
		model.addAttribute("films", filmrepo.findAll());
		model.addAttribute("cameras", camerarepo.findAll()); // kamerat ja filmit tietokannasta templatelle
		return "editphoto"; // editphoto.html
	
	}
	// Save update
	@PostMapping("/update/{id}")
	public String updatePhoto(@PathVariable("id") Long id, Model model, Photograph photo) {
		photorepo.save(photo);
		
		return "redirect:/allphotos";
		
	}
	
}
