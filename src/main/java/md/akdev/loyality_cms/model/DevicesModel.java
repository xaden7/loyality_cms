package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DEVICES")
public class DevicesModel {

  @Column
  private UUID clientId;
  @Id
  @Column
  @NotEmpty
  private String deviceId;
  @Column
  private String deviceName;
  @Column
  private String os;
  @Column
  private String osVersion;
  @Column
  private String appVersion;
  @Column
  private String appBuild;
  @Column
  private String appPackage;
  @Column
  private String fcmToken;
  @Column
  private LocalDateTime lastConnect;

  @Override
  public String toString() {

    String clientIdStr = this.clientId != null ? this.clientId.toString() : "N/A";

    return "DevicesModel{" +
            "clientId=" + clientIdStr +
            ", deviceId='" + deviceId + '\'' +
            ", deviceName='" + deviceName + '\'' +
            ", os='" + os + '\'' +
            ", osVersion='" + osVersion + '\'' +
            ", appVersion='" + appVersion + '\'' +
            ", appBuild='" + appBuild + '\'' +
            ", appPackage='" + appPackage + '\'' +
            ", fcmToken='" + fcmToken + '\'' +
            '}';
  }
}
