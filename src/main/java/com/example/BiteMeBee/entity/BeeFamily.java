package com.example.BiteMeBee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@Table("bee_families")
@NoArgsConstructor
@AllArgsConstructor
public class BeeFamily {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "bee_type_id", nullable = false)
  private BeeType beeType;

  @Column(name="note", nullable=false)
  private String note;

  @Column(name="status", nullable=false)
  private Boolean status;

}
