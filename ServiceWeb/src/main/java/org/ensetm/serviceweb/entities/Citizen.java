package org.ensetm.serviceweb.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Citizen implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String nom;
    @Column(length = 25)
    private String prenom;
    private String adresse;
    @Column(length = 15)
    private String ville;
    private String numTele;
    @Column(unique = true)
    private String id_device;
    @OneToMany(mappedBy = "citizen")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<GpsLog> gpsLogCollection;
}
