package com.inkrecords.repository;

import com.inkrecords.model.RecordCategory;
import com.inkrecords.model.RecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<RecordEntry, Long> {

    List<RecordEntry> findAllByOrderByUpdatedAtDesc();

    List<RecordEntry> findAllByCategoryOrderByUpdatedAtDesc(RecordCategory category);
}
