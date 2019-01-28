package com.embarque.embarque.model;

import com.embarque.embarque.enums.Cargo;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa {

  private Cargo cargo;

  public Pessoa(String cargo) {
    this.cargo = Cargo.valueOf(cargo);
  }
}
