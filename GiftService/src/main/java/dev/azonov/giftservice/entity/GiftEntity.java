package dev.azonov.giftservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
@Data
public class GiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private Integer quantity;

    @Basic
    @Column(length = 50, unique = true)
    private String kind;
}
