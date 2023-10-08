package model;

import java.time.LocalDateTime;

public class Review {
    private Consumer author;
    private Event event;
    private LocalDateTime creationDateTime;
    private String content;

    public Review(Consumer author, Event event, LocalDateTime creationDateTime, String content) {
        this.author = author;
        this.event = event;
        this.creationDateTime = creationDateTime;
        this.content = content;
    }

    public Consumer getAuthor() {
        return author;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author=" + author +
                ", event=" + event +
                ", creationDateTime=" + creationDateTime +
                ", content='" + content + '\'' +
                '}';
    }
}
