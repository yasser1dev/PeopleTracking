package org.ensetm.serviceweb;

import org.ensetm.serviceweb.dao.ICitizensRepository;
import org.ensetm.serviceweb.services.IDataInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServicewebApplication implements CommandLineRunner {
    @Autowired
    private IDataInit dataInit;

    public static void main(String[] args){
        SpringApplication.run(ServicewebApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
       dataInit.initCitizens();
    }
}
