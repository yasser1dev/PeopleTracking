package org.ensetm.serviceweb.dao;

import org.ensetm.serviceweb.entities.GpsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.ws.rs.QueryParam;
import java.util.Collection;
import java.util.Date;

@RepositoryRestResource
public interface IGpsLogRepository extends JpaRepository<GpsLog,Long> {
    @Query("select g from GpsLog g inner join Citizen  c on c.id=g.citizen.id where g.dateTime>=:date2 and g.dateTime<=:date1")
    Collection<GpsLog> findGpsDataLastDays(@Param("date2") Date date2,@Param("date1") Date date1);

    @Query("select g from GpsLog g inner join Citizen  c on c.id=:id where g.dateTime>=:date2 and g.dateTime<=:date1")
    Collection<GpsLog> findGpsDataLastDaysById(@Param("date2") Date date2,@Param("date1") Date date1,@Param("id") Long id);
    //select * from gps_log g inner join citizen  c on c.id=4 where g.date_time>="2020-06-23" and g.date_time<="2020-06-27"



}
