package org.enset.dao;


import org.ensetm.serviceweb.entities.GpsLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGPSLogRepository extends JpaRepository<GpsLog,Long> {
}
