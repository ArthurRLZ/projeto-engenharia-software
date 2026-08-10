package br.edu.ufape.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.backend.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
