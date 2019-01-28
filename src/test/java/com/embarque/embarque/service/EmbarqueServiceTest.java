package com.embarque.embarque.service;

import com.embarque.embarque.EmbarqueApplication;
import com.embarque.embarque.enums.Cargo;
import com.embarque.embarque.exception.PassageirosNaoPermitidosException;
import com.embarque.embarque.exception.TransporteLotadoException;
import com.embarque.embarque.exception.TripulacaoIncorretaException;
import com.embarque.embarque.model.Pessoa;
import com.embarque.embarque.model.SalaEmbarque;
import com.embarque.embarque.model.SmartFortwo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmbarqueApplication.class)
public class EmbarqueServiceTest {

  @Autowired
  private SmartFortwoService smartFortwoService;
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final int QUANTIDADE_PESSOAS_ORIGINAL = 8;
  SmartFortwoService service = new SmartFortwoService();
  SmartFortwo smartFortwo = new SmartFortwo();
  Pessoa chefeServico = Pessoa.builder()
          .cargo(Cargo.CHEFE_DE_SERVICO)
          .build();

  Pessoa policial = Pessoa.builder()
          .cargo(Cargo.POLICIAL)
          .build();

  Pessoa comissaria = Pessoa.builder()
          .cargo(Cargo.COMISSARIA)
          .build();

  Pessoa segundaComissaria = Pessoa.builder()
          .cargo(Cargo.COMISSARIA)
          .build();

  Pessoa piloto = Pessoa.builder()
          .cargo(Cargo.PILOTO)
          .build();

  Pessoa oficialUm = Pessoa.builder()
          .cargo(Cargo.OFICIAL)
          .build();

  Pessoa oficialDois = Pessoa.builder()
          .cargo(Cargo.OFICIAL)
          .build();

  Pessoa bandido = Pessoa.builder()
          .cargo(Cargo.PRESIDIARIO)
          .build();
  SalaEmbarque salaEmbarque =  SalaEmbarque.builder()
          .filaDeEspera(Arrays.asList(chefeServico,policial,comissaria,
  segundaComissaria, piloto, oficialUm, oficialDois, bandido))
          .build();

    @Test
    public void devePossuiOitoPessoasNaSalaDeEmbarque() {
      int quantidadePessoas = salaEmbarque.getFilaDeEspera().size();
      Assert.assertEquals(quantidadePessoas, QUANTIDADE_PESSOAS_ORIGINAL);
    }

    @Test
    public void deveRetonarErroAoEmbarcarPilotoComComissariaRestritosNoSmartTwo() throws TripulacaoIncorretaException, PassageirosNaoPermitidosException, TransporteLotadoException {
      smartFortwo.setMotorista(piloto);
      smartFortwo.setPassageiro(comissaria);
      thrown.expect(TripulacaoIncorretaException.class);
      smartFortwoService.transportarPassageiro(smartFortwo, salaEmbarque);
    }

  @Test
  public void deveRetonarErroAoEmbarcarOficialComChefeDeServicoRestritosNoSmartTwo() throws TripulacaoIncorretaException, PassageirosNaoPermitidosException, TransporteLotadoException {
    smartFortwo.setMotorista(chefeServico);
    smartFortwo.setPassageiro(oficialDois);
    thrown.expect(TripulacaoIncorretaException.class);
    smartFortwoService.transportarPassageiro(smartFortwo, salaEmbarque);
  }

  @Test
  public void naoDeveEmbarcarPilotoNoAviaoPorqueJaTemUmaComissariaSozinha() throws TripulacaoIncorretaException, PassageirosNaoPermitidosException, TransporteLotadoException {
    smartFortwo.setMotorista(chefeServico);
    smartFortwo.setPassageiro(comissaria);
    smartFortwoService.transportarPassageiro(smartFortwo, salaEmbarque);
    smartFortwo.setMotorista(chefeServico);
    smartFortwo.setPassageiro(piloto);
    thrown.expect(PassageirosNaoPermitidosException.class);
    smartFortwoService.transportarPassageiro(smartFortwo, salaEmbarque);
  }

}
