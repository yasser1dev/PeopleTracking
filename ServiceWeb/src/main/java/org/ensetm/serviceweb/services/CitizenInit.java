package org.ensetm.serviceweb.services;

import org.ensetm.serviceweb.dao.ICitizensRepository;
import org.ensetm.serviceweb.entities.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CitizenInit  implements IDataInit{
    @Autowired
    ICitizensRepository citizensRepository;
    @Override
    public void initCitizens() {
        for (int i = 1; i < 500 ; i++) {
            Citizen citizen=new Citizen();
            citizen.setPrenom("p"+i);
            citizen.setNom("p"+i);
            citizen.setId_device("device"+i);
            citizen.setVille("Casablanca");
            citizensRepository.save(citizen);
        }

    }
}
