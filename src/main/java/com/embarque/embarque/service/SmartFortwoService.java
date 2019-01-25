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

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class SmartFortwoService {

  SmartFortwo smartFortwo = new SmartFortwo();
  SalaEmbarque salaEmbarque = new SalaEmbarque();
  Aviao aviao = new Aviao();

  public void transportarPassageiro(Pessoa motorista, Pessoa passageira) throws TransporteLotadoException, TripulacaoIncorretaException {
    if (!ObjectUtils.isEmpty(smartFortwo)
            && smartFortwo.getLugar().equals(Lugar.SALA_ESPERA)) {
      if (validarPermissaoEmbarque(motorista, passageira)) {
        smartFortwo.setMotorista(motorista);
        smartFortwo.setPassageiro(passageira);
        embarcarPassageiro(smartFortwo.getPassageiro());
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
            .put("PILOTO", cargo -> !cargo.equals("COMISSARIA"))
            .put("POLICIAL", cargo -> cargo.equals("BANDIDO"))
            .put("CHEFE_DE_SERVICO", cargo -> !cargo.equals("OFICIAL"))
            .build();

    boolean valido =
            perfilValidator.get(motorista.getCargo())
                    .test(passageira.getCargo());
    if (valido) {

    } else {
      throw new TripulacaoIncorretaException("Essa tripulação não pode ser transportada");
    }
    return valido;
  }

  public void embarcarPassageiro(Pessoa passageiro) {
    if (!ObjectUtils.isEmpty(passageiro)
            && smartFortwo.getLugar().equals(Lugar.AVIAO)) {
      aviao.setPessoaEmbarque(passageiro);
      setLugarSalaEspera();
    }
  }

  public void setLugarSalaEspera() {
    smartFortwo.setLugar(Lugar.SALA_ESPERA);
  }

  public void setLugarAviao() {
    smartFortwo.setLugar(Lugar.AVIAO);
  }

}
