package com.railsync.document.repository;

import com.railsync.document.domain.RailwayDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RailwayDocumentRepository extends JpaRepository<RailwayDocument, Long> {
    Optional<RailwayDocument> findByDocIdentifier(String docIdentifier);
}
