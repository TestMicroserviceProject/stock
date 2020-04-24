package com.epam.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
  @SequenceGenerator(name = "items_seq", sequenceName = "items_id_seq", allocationSize = 1)
  private Long id;

  @Column
  private String name;

  @Column
  private Integer amount;

  @Column
  private Double price;
}
