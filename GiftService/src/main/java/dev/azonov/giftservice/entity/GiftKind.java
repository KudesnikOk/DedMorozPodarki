package dev.azonov.giftservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "giftkind")
public class GiftKind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(length = 50)
    private String name;
}
