package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Column(name = "code", length = 30)
    private String code;

    @Size(max = 30)
    @Column(name = "city", length = 30)
    private String city;

    @Size(max = 30)
    @Column(name = "region", length = 30)
    private String region;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 30)
    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Size(max = 100)
    @Column(name = "working_hours", length = 100)
    private String workingHours;

    @Size(max = 100)
    @Column(name = "working_saturday_hours", length = 100)
    private String workingSaturdayHours;

    @Size(max = 100)
    @Column(name = "working_sunday_hours", length = 100)
    private String workingSundayHours;

    @Column(name = "optics")
    private Boolean optics;

    @Size(max = 30)
    @Column(name = "optics_phone", length = 30)
    private String opticsPhone;

    @Size(max = 100)
    @Column(name = "optics_working_hours", length = 100)
    private String opticsWorkingHours;

    @Size(max = 100)
    @Column(name = "optics_working_saturday_hours", length = 100)
    private String opticsWorkingSaturdayHours;

    @Size(max = 100)
    @Column(name = "optics_working_sunday_hours", length = 100)
    private String opticsWorkingSundayHours;

    @Column(name = "created_at")
    private Instant createdAt;

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", workingHours='" + workingHours + '\'' +
                ", workingSaturdayHours='" + workingSaturdayHours + '\'' +
                ", workingSundayHours='" + workingSundayHours + '\'' +
                ", optics=" + optics +
                ", opticsPhone='" + opticsPhone + '\'' +
                ", opticsWorkingHours='" + opticsWorkingHours + '\'' +
                ", opticsWorkingSaturdayHours='" + opticsWorkingSaturdayHours + '\'' +
                ", opticsWorkingSundayHours='" + opticsWorkingSundayHours + '\'' +
                '}';
    }
}
