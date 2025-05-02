package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notification_user")
public class NotificationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "type", length = 100)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Size(max = 200)
    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "body", length = Integer.MAX_VALUE)
    private String body;

    @Size(max = 200)
    @Column(name = "topic", length = 200)
    private String topic;

    @Column(name = "icon", length = Integer.MAX_VALUE)
    private String icon;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "is_read")
    private Boolean isRead;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static NotificationUserBuilder builder() {
        return new NotificationUserBuilder();
    }

    public static class NotificationUserBuilder {
        private Integer id;
        private UUID userId;
        private NotificationType type;
        private String title;
        private String body;
        private String topic;
        private String icon;
        private String url;
        private Boolean isRead = false;

        public NotificationUserBuilder setIsRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public NotificationUserBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public NotificationUserBuilder setUserId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public NotificationUserBuilder setType(NotificationType type) {
            this.type = type;
            return this;
        }

        public NotificationUserBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public NotificationUserBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public NotificationUserBuilder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public NotificationUserBuilder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public NotificationUserBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public NotificationUser build() {
            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setId(id);
            notificationUser.setUserId(userId);
            notificationUser.setType(type);
            notificationUser.setTitle(title);
            notificationUser.setBody(body);
            notificationUser.setTopic(topic);
            notificationUser.setIcon(icon);
            notificationUser.setUrl(url);
            notificationUser.setIsRead(isRead);
            return notificationUser;
        }
    }

}