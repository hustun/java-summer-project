package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Language;

public interface LanguageService extends BaseService<Language, Integer> {
    Language findByLanguageName(String languageName);

}
