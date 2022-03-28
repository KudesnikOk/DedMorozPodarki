package dev.azonov.giftservice.repository;

import dev.azonov.giftservice.entity.GiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends JpaRepository<GiftEntity, Long> {
    GiftEntity findFirstByKind(String kind);
}
