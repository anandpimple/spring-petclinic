package org.springframework.samples.petclinic.vet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

public class SpecialtyTypeFormatter implements Formatter<Specialty> {

    private final SpecialityRepository specialityRepository;


    @Autowired
    public SpecialtyTypeFormatter(SpecialityRepository specialityRepository) {

        this.specialityRepository = specialityRepository;
    }

    @Override
    public String print(Specialty specialty, Locale locale) {
        return specialty.getName();
    }

    @Override
    public Specialty parse(String text, Locale locale) throws ParseException {
        Collection<Specialty> specialties = this.specialityRepository.findAll();
        for (Specialty specialty : specialties) {
            if (specialty.getName().equals(text)) {
                return specialty;
            }
        }
        throw new ParseException("type not found: " + text, 0);
    }

}
