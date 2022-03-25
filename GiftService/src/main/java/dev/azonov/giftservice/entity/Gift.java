package dev.azonov.giftservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "kindId", referencedColumnName = "id", nullable = false, unique = true)
    private GiftKind kind;
}
