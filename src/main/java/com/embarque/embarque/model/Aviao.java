package com.embarque.embarque.model;

import com.embarque.embarque.enums.Lugar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aviao {

  private List<Pessoa> pessoasEmbarcadas = new ArrayList<>();

  public void setPessoaEmbarque(Pessoa pessoa) {
    pessoasEmbarcadas.add(pessoa);
  }
}
