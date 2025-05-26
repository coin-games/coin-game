package com.cgs.backend.repository;

import com.cgs.backend.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecordRepository extends JpaRepository<UserRecord, String> {
}
