package jar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Translation;
import jar.repo.TranslationRepo;

@RestController
@RequestMapping("/translations")
public class TranslationController {

    private final TranslationRepo repo;

    public TranslationController(TranslationRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Translation> getAllTranslations() {
        return repo.findAll();
    }
}