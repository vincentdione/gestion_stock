package com.ovd.gestionstock.repositories;

import com.ovd.gestionstock.models.ConditionAV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionAVRepository extends TenantAwareRepository<ConditionAV,Long> {
    List<ConditionAV> findByArticleId(Long articleId);
}
