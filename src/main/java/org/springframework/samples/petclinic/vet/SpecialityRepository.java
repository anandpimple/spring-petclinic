package org.springframework.samples.petclinic.vet;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
public interface SpecialityRepository extends Repository<Specialty,Long> {
    /**
     * Retrieve all <code>Speciality</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>Speciality</code>s
     */
    @Transactional(readOnly = true)
    @Cacheable("specialties")
    Collection<Specialty> findAll() throws DataAccessException;
}
