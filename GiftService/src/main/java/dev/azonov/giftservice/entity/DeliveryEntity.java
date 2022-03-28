package dev.azonov.giftservice.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Information about that gift of some kind is send to a child
 */
@Entity
@Table(name = "deliveries")
@Data
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "childId", nullable = false)
    private ChildEntity child;

    @ManyToOne
    @JoinColumn(name = "giftId", nullable = false)
    private GiftEntity gift;
}
