package org.ensetm.serviceweb.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(exclude = "citizen")
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class GpsLog implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private Date dateTime;
    private double speed;
    @ManyToOne
    private Citizen citizen;
}
