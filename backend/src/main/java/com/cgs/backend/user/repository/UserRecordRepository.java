package com.cgs.backend.user.repository;

import com.cgs.backend.user.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecordRepository extends JpaRepository<UserRecord, String> {
}
