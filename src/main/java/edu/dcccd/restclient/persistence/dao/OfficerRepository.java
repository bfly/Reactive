package edu.dcccd.restclient.persistence.dao;

import edu.dcccd.restclient.persistence.entities.Officer;
import edu.dcccd.restclient.persistence.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findByRank(@Param("rank") Rank rank);
    List<Officer> findByLast( @Param("last") String last );
}
