package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Long views;
}
