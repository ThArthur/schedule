package dev.rokku.schedule.domain.model.room_block;

import dev.rokku.schedule.domain.model.room.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "room_block")
@Getter
@Setter
@NoArgsConstructor
public class RoomBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    private String reason;

    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
