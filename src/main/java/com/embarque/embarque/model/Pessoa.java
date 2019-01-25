package com.embarque.embarque.model;

import com.embarque.embarque.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
