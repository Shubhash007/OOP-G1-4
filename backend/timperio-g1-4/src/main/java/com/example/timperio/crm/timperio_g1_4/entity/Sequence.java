package com.example.timperio.crm.timperio_g1_4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sequence {

    @Id
    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "next_id", nullable = false)
    private Integer nextId;
}
