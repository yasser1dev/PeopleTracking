package org.ensetm.serviceweb.dao;

import org.ensetm.serviceweb.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ICitizensRepository extends JpaRepository<Citizen,Long> {
}
