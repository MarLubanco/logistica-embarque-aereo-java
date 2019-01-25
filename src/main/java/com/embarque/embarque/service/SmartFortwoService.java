package com.embarque.embarque.service;

import ch.qos.logback.classic.net.SMTPAppender;
import com.embarque.embarque.enums.Cargo;
import com.embarque.embarque.enums.Lugar;
import com.embarque.embarque.exception.TransporteLotadoException;
import com.embarque.embarque.exception.TripulacaoIncorretaException;
import com.embarque.embarque.model.Aviao;
import com.embarque.embarque.model.Pessoa;
import com.embarque.embarque.model.SalaEmbarque;
import com.embarque.embarque.model.SmartFortwo;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class SmartFortwoService {

  SalaEmbarque salaEmbarque = new SalaEmbarque();
  Aviao aviao = new Aviao();

  public void transportarPassageiro(SmartFortwo smartFortwo) throws TransporteLotadoException, TripulacaoIncorretaException {
    if (!ObjectUtils.isEmpty(smartFortwo)
            && smartFortwo.getLugar().equals(Lugar.SALA_ESPERA)) {
      if (validarPermissaoEmbarque(smartFortwo.getMotorista(), smartFortwo.getPassageiro())) {
        smartFortwo.setLugar(Lugar.AVIAO);
        embarcarPassageiro(smartFortwo);
      } else {
        throw new TripulacaoIncorretaException("Não é permitida essa tripulação");
      }
    } else {
      throw new TransporteLotadoException("O SmartFortwo está com todos os lugares ocupados");
    }
  }

  public void validarPermissoesLugares(List<Pessoa> pessoas) {
    pessoas.stream()
            .map(pessoa -> {
              if (pessoa.getCargo().equals(Cargo.PILOTO)) {
                return pessoas.stream()
                        .anyMatch(pessoaCargo -> pessoaCargo.getCargo().equals(Cargo.COMISSARIA));
              }
              if (pessoa.getCargo().equals(Cargo.CHEFE_DE_SERVICO)) {
                return pessoas.stream()
                        .anyMatch(pessoaCargo -> pessoaCargo.getCargo().equals(Cargo.OFICIAL));
              } else {
                return true;
              }
            });
  }

  public boolean validarPermissaoEmbarque(Pessoa motorista, Pessoa passageira) throws TripulacaoIncorretaException {
    Map<String, Predicate<Cargo>> perfilValidator = ImmutableMap.<String, Predicate<Cargo>>builder()
            .put("PILOTO", cargo -> !cargo.getNome().equals("COMISSARIA"))
            .put("POLICIAL", cargo -> cargo.getNome().equals("PRESIDIARIO"))
            .put("CHEFE_DE_SERVICO", cargo -> !cargo.getNome().equals("OFICIAL"))
            .build();

   return perfilValidator.get(motorista.getCargo().getNome().toUpperCase())
                    .test(passageira.getCargo());
  }

  public void embarcarPassageiro(SmartFortwo smartFortwo) {
    if (!ObjectUtils.isEmpty(smartFortwo)
            && smartFortwo.getLugar().equals(Lugar.AVIAO)) {
      aviao.setPessoaEmbarque(smartFortwo.getPassageiro());
      setLugarSalaEspera(smartFortwo);
    }
  }

  public void setLugarSalaEspera(SmartFortwo smartFortwo) {
    smartFortwo.setLugar(Lugar.SALA_ESPERA);
  }

  public void setLugarAviao(SmartFortwo smartFortwo) {
    smartFortwo.setLugar(Lugar.AVIAO);
  }

}
