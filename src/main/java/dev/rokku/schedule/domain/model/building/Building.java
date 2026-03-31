package dev.rokku.schedule.domain.model.building;

import dev.rokku.schedule.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "building")
@Getter
@Setter
@NoArgsConstructor
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String number;

    private String complement;

    @Column(name = "image_url")
    private String imageUrl;
}