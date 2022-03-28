package dev.azonov.giftservice.repository;

import dev.azonov.giftservice.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
    ChildEntity findFirstByFirstNameAndSecondNameAndMiddleName(String firstName, String secondName, String middleName);
}
