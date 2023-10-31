package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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



}
