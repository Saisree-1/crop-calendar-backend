package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Language;
import jar.repo.LanguageRepo;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    @Autowired
    private LanguageRepo languageRepo;

    @GetMapping
    public List<Language> getLanguages() {
        return languageRepo.findAll();
    }
}