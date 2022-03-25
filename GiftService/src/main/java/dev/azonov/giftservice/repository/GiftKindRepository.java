package dev.azonov.giftservice.repository;

import dev.azonov.giftservice.entity.GiftKind;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftKindRepository extends JpaRepository<GiftKind, Long> {

}
