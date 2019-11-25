package edu.dcccd.restclient.persistence.dao;

import edu.dcccd.restclient.persistence.entities.Officer;
import edu.dcccd.restclient.persistence.entities.Rank;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OfficerRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private OfficerRepository repository;

    private Logger log;

    @Test
    public void logDB() {
        log = LoggerFactory.getLogger(OfficerRepositoryTest.class);
        List<Officer> officers = repository.findAll();
        log.info(String.valueOf(officers.size()));
        officers.stream().forEach(o -> log.info(o.toString()));
    }

    @Test
    public void testSave() throws Exception {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = repository.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    public void findById() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
            .forEach(id -> {
                Optional<Officer> officer = repository.findById(id);
                assertTrue(officer.isPresent());
                assertEquals(id, officer.get().getId());
            });
    }

    @Test
    public void findAll() throws Exception {
        List<String> dbNames = repository.findAll().stream()
            .map(Officer::getLast)
            .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count() throws Exception {
        assertEquals(5, repository.count());
    }

    @Test
    public void deleteById() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
            .forEach(id -> repository.deleteById(id));
        assertEquals(0, repository.count());
    }

    @Test
    public void existsById() throws Exception {
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
            .forEach(id -> assertTrue(repository.existsById(id), String.format("%d should exist", id)));
    }

    @Test
    public void doesNotExist() {
        List<Integer> ids = template.query("select id from officers",
            (rs, num) -> rs.getInt("id"));
        assertThat(ids, not(contains(999)));
        assertFalse(repository.existsById(999));
    }

    @Test
    public void findByRank() throws Exception {
        repository.findByRank(Rank.CAPTAIN).forEach(captain ->
            assertEquals(Rank.CAPTAIN, captain.getRank()));

    }

    @Test
    public void findByLast() throws Exception {
        List<Officer> kirks = repository.findByLast("Kirk");
        assertEquals(1, kirks.size());
        assertEquals("Kirk", kirks.get(0).getLast());
    }
}
