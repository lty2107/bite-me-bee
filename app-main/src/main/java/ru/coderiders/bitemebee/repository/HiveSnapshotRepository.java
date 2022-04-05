package ru.coderiders.bitemebee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coderiders.bitemebee.entity.HiveSnapshot;

import java.time.Instant;
import java.util.List;

public interface HiveSnapshotRepository extends JpaRepository<HiveSnapshot, Long> {
    List<HiveSnapshot> findByCreatedAtBetweenAndHive_Id(Instant createdAtBegin, Instant createdAtEnd, Long id);
}