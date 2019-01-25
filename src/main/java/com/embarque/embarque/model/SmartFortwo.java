package com.embarque.embarque.model;

import com.embarque.embarque.enums.Lugar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartFortwo {

  private Pessoa motorista;

  private Pessoa passageiro;

  private Lugar lugar;
}
