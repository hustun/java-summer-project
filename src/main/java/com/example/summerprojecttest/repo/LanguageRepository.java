package com.example.summerprojecttest.repo;

import com.example.summerprojecttest.model.Language;
import org.springframework.data.repository.CrudRepository;

public interface LanguageRepository extends CrudRepository<Language, Integer> {
    Language findByLanguageName(String languageName);

}
