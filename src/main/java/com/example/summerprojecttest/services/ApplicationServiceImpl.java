package com.example.summerprojecttest.services;

import com.example.summerprojecttest.model.Application;
import com.example.summerprojecttest.repo.ApplicationRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Set<Application> findAll() {
        Set<Application> applications = new HashSet<>();
        applicationRepository.findAll().forEach(applications::add);
        return applications;
    }

    @Override
    public Application findById(Integer id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);

        if (optionalApplication.isPresent()){
            return optionalApplication.get();
        }else{
            return null;
        }
    }

    @Override
    public Application save(Application object) {
        return applicationRepository.save(object);
    }

    @Override
    public void delete(Application object) {
        applicationRepository.delete(object);
    }

    @Override
    public void deleteById(Integer id) {
        applicationRepository.deleteById(id);
    }
}
