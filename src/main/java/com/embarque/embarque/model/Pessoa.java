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

  private Long id;

  private Cargo cargo;

}
