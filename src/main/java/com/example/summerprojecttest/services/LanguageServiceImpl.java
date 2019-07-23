package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Language;
import com.example.summerprojecttest.repo.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LanguageServiceImpl implements LanguageService{

    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language findByLanguageName(String languageName) {
        return languageRepository.findByLanguageName(languageName);
    }

    @Override
    public Set<Language> findAll() {
        Set<Language> languages = new HashSet<>();

        languageRepository.findAll().forEach((languages::add));

        return languages;
    }

    @Override
    public Language findById(Integer id) {
        Optional<Language> optionalLanguage = languageRepository.findById(id);

        if (optionalLanguage.isPresent()){
            return optionalLanguage.get();
        }else {
            return null;
        }
    }

    @Override
    public Language save(Language object) {
        return languageRepository.save(object);
    }

    @Override
    public void delete(Language object) {
        languageRepository.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        languageRepository.deleteById(id);
    }
}
