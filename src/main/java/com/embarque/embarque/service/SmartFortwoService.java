package com.embarque.embarque.service;

import com.embarque.embarque.enums.Cargo;
import com.embarque.embarque.exception.PassageirosNaoPermitidosException;
import com.embarque.embarque.exception.TransporteLotadoException;
import com.embarque.embarque.exception.TripulacaoIncorretaException;
import com.embarque.embarque.model.Aviao;
import com.embarque.embarque.model.Pessoa;
import com.embarque.embarque.model.SalaEmbarque;
import com.embarque.embarque.model.SmartFortwo;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SmartFortwoService {

  Aviao aviao = new Aviao();

  public void transportarPassageiro(SmartFortwo smartFortwo, SalaEmbarque salaEmbarque) throws TransporteLotadoException, TripulacaoIncorretaException, PassageirosNaoPermitidosException {
    if (!ObjectUtils.isEmpty(smartFortwo)) {
      if (validarPermissaoePermanenciaLocais(salaEmbarque.getFilaDeEspera())
              && validarPermissaoePermanenciaLocais(aviao.getPessoasEmbarcadas())
              && validarPermissoesTransporte(smartFortwo.getMotorista(), smartFortwo.getPassageiro())) {
        embarcarPassageiro(smartFortwo, salaEmbarque);
      } else {
        throw new TripulacaoIncorretaException("Não é permitida essa tripulação");
      }
    } else {
      throw new TransporteLotadoException("O SmartFortwo está com todos os lugares ocupados");
    }
  }

  public void transportarPassageiroParaEmbarque(SmartFortwo smartFortwo, SalaEmbarque salaEmbarque) throws TripulacaoIncorretaException, PassageirosNaoPermitidosException, TransporteLotadoException {
    if (!ObjectUtils.isEmpty(smartFortwo)) {
      if (validarPermissaoePermanenciaLocais(aviao.getPessoasEmbarcadas()) &&
              validarPermissoesTransporte(smartFortwo.getMotorista(), smartFortwo.getPassageiro())) {
        desembarcarPassageiroParaFilaEmbarque(smartFortwo, salaEmbarque);
      } else {
        throw new TripulacaoIncorretaException("Não é permitida essa tripulação");
      }
    } else {
      throw new TransporteLotadoException("O SmartFortwo está com todos os lugares ocupados");
    }
  }

  public boolean validarPermissaoePermanenciaLocais(List<Pessoa> pessoas) throws PassageirosNaoPermitidosException {
    boolean valido = false;
    if (pessoas.size() == 2) {
      Map<String, Predicate<Cargo>> perfilValidator = retornarValidacoes();

      valido = perfilValidator.get(pessoas.get(0).getCargo().getNome().toUpperCase())
              .test(pessoas.get(1).getCargo())
              || perfilValidator.get(pessoas.get(1).getCargo().getNome().toUpperCase())
              .test(pessoas.get(0).getCargo());
      if (!valido) {
        throw new PassageirosNaoPermitidosException("Não permitido a tripulação no avião");
      }
    }
    return true;
  }

  public ImmutableMap<String, Predicate<Cargo>> retornarValidacoes() {
    return ImmutableMap.<String, Predicate<Cargo>>builder()
            .put("OFICIAL", cargo -> !cargo.getNome().equals("CHEFE_DE_SERVICO"))
            .put("PILOTO", cargo -> !cargo.getNome().equals("COMISSARIA"))
            .put("PRESIDIARIO", cargo -> cargo.getNome().equals("POLICIAL"))
            .put("CHEFE_DE_SERVICO", cargo -> !cargo.getNome().equals("OFICIAL"))
            .put("COMISSARIA", cargo -> !cargo.getNome().equals("PILOTO"))
            .put("POLICIAL", cargo -> cargo.getNome().equals("PRESIDIARIO"))
            .build();
  }

  public boolean validarPermissoesTransporte(Pessoa motorista, Pessoa passageira) throws TripulacaoIncorretaException {
    Map<String, Predicate<Cargo>> perfilValidator = ImmutableMap.<String, Predicate<Cargo>>builder()
            .put("PILOTO", cargo -> !cargo.getNome().equals("COMISSARIA"))
            .put("POLICIAL", cargo -> cargo.getNome().equals("PRESIDIARIO"))
            .put("CHEFE_DE_SERVICO", cargo -> !cargo.getNome().equals("OFICIAL"))
            .build();

    return perfilValidator.get(motorista.getCargo().getNome().toUpperCase())
            .test(passageira.getCargo());
  }

  public void embarcarPassageiro(SmartFortwo smartFortwo, SalaEmbarque salaEmbarque) throws PassageirosNaoPermitidosException {
    if (!ObjectUtils.isEmpty(smartFortwo)) {
      aviao.setPessoaEmbarque(smartFortwo.getPassageiro());
      removePassageiro(salaEmbarque, smartFortwo.getPassageiro());
      log.info("Passageiro ".concat(smartFortwo.getPassageiro().getCargo().getNome()).concat(" embarcado no avião"));
      smartFortwo.setPassageiro(null);
      validarPermissaoePermanenciaLocais(aviao.getPessoasEmbarcadas());
      log.info("Lista de passageiros no avião: ");
      listaPassageirosSalaEmbarqueAviao(aviao, salaEmbarque);
    }
  }

  public void desembarcarPassageiroParaFilaEmbarque(SmartFortwo smartFortwo, SalaEmbarque salaEmbarque) throws PassageirosNaoPermitidosException {
    List<Pessoa> pessoasAtualizadas = salaEmbarque.getFilaDeEspera();
    pessoasAtualizadas.add(smartFortwo.getPassageiro());
    salaEmbarque.setFilaDeEspera(pessoasAtualizadas);
    removerPassageiroEmbarcadoAviao(aviao, smartFortwo.getPassageiro());
    log.info("Passageiro ".concat(smartFortwo.getPassageiro().getCargo().getNome()).concat(" embarcado no avião"));
    smartFortwo.setPassageiro(null);
    validarPermissaoePermanenciaLocais(salaEmbarque.getFilaDeEspera());
    log.info("Lista de passageiros do embarque: ");
    listaPassageirosSalaEmbarqueAviao(aviao, salaEmbarque);

  }

  private List<Pessoa> gerarListaAtualizada(List<Pessoa> pessoas, Pessoa pessoa) {
    return pessoas.stream()
            .filter(passageiro -> !passageiro.getCargo().getNome()
                    .equalsIgnoreCase(pessoa.getCargo().getNome()))
            .collect(Collectors.toList());
  }

  private void removerPassageiroEmbarcadoAviao(Aviao aviao, Pessoa pessoa) {
    List<Pessoa> listaPessoas = gerarListaAtualizada(aviao.getPessoasEmbarcadas(), pessoa);
    aviao.setPessoasEmbarcadas(listaPessoas);
  }

  public void removePassageiro(SalaEmbarque salaEmbarque, Pessoa pessoa) {
    List<Pessoa> listaPessoas = gerarListaAtualizada(salaEmbarque.getFilaDeEspera(), pessoa);
    salaEmbarque.setFilaDeEspera(listaPessoas);
  }

  public void listaPassageirosSalaEmbarqueAviao(Aviao aviao, SalaEmbarque salaEmbarque) {
    aviao.getPessoasEmbarcadas().forEach(pessoa -> log.info("Passageiro embarcado: ".concat(pessoa.getCargo().getNome())));
    log.info("Lista de pessoas na sala de embarque: ");
    salaEmbarque.getFilaDeEspera().forEach(pessoa -> log.info("Passageiro: ".concat(pessoa.getCargo().getNome())));
  }
}
