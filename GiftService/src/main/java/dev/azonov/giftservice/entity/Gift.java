package dev.azonov.giftservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
@Data
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private Integer quantity;

    @Basic
    @Column(length = 50)
    private String kind;
}
