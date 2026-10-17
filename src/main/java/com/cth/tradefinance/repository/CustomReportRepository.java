package com.cth.tradefinance.repository;

import com.cth.tradefinance.model.CustomReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomReportRepository extends JpaRepository<CustomReport, Long> {
}
