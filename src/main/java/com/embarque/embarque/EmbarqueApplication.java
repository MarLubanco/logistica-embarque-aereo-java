package com.embarque.embarque;

import com.embarque.embarque.enums.Cargo;
import com.embarque.embarque.enums.Lugar;
import com.embarque.embarque.exception.PassageirosNaoPermitidosException;
import com.embarque.embarque.exception.TransporteLotadoException;
import com.embarque.embarque.exception.TripulacaoIncorretaException;
import com.embarque.embarque.model.Pessoa;
import com.embarque.embarque.model.SalaEmbarque;
import com.embarque.embarque.model.SmartFortwo;
import com.embarque.embarque.service.SmartFortwoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class EmbarqueApplication {

	public static void main(String[] args) throws TransporteLotadoException, TripulacaoIncorretaException, PassageirosNaoPermitidosException {
		SpringApplication.run(EmbarqueApplication.class, args);

		SmartFortwoService service = new SmartFortwoService();
		SmartFortwo smartFortwo = new SmartFortwo();
		SalaEmbarque salaEmbarque = new SalaEmbarque();
		Pessoa chefeServico = new Pessoa(Cargo.CHEFE_DE_SERVICO);
		Pessoa policial = new Pessoa(Cargo.POLICIAL);
		Pessoa comissaria = new Pessoa(Cargo.COMISSARIA);
		Pessoa segundaComissaria = new Pessoa(Cargo.COMISSARIA);
		Pessoa piloto = new Pessoa(Cargo.PILOTO);
		Pessoa oficialUm = new Pessoa(Cargo.OFICIAL);
		Pessoa oficialDois = new Pessoa(Cargo.OFICIAL);
		Pessoa bandido = new Pessoa(Cargo.PRESIDIARIO);
		salaEmbarque.setFilaDeEspera(Arrays.asList(chefeServico,policial,comissaria,
						segundaComissaria, piloto, oficialUm, oficialDois, bandido));
		while (true) {
			System.out.println("------------------------------------------");
			System.out.println("Escolha a ação no aeroporto: ");
			System.out.println();
			System.out.println("1 - EMBARCAR NO AVIAO ");
			System.out.println("2 - DESEMBARCAR PARA A FILA DE EMBARQUE ");
			System.out.println("------------------------------------------");
			Scanner scanner = new Scanner(System.in);
			String comando = scanner.nextLine();

			switch (comando) {
				case "1":
					System.out.println("Transporte para o avião ");
					System.out.print("Cargo da pessoa motorista: ");
					String cargoMotorista = scanner.next();
					Pessoa motorista = new Pessoa(cargoMotorista);
					System.out.print("Cargo da pessoa passageiro: ");
					String cargoPassageiro = scanner.next();
					Pessoa passageiro = new Pessoa(cargoPassageiro);
					smartFortwo.setLugar(Lugar.SALA_ESPERA);
					smartFortwo.setMotorista(motorista);
					smartFortwo.setPassageiro(passageiro);
					service.transportarPassageiro(smartFortwo, salaEmbarque);
					break;

				case "2" :
					System.out.println("Transporte para sala de embarque");
					System.out.print("Cargo da pessoa motorista: ");
					String cargo = scanner.next();
					Pessoa motoristaEmbarque = new Pessoa(cargo);
					System.out.print("Cargo da pessoa passageiro: ");
					String cargoPass = scanner.next();
					Pessoa passageiroEmbarque = new Pessoa(cargoPass);
					smartFortwo.setLugar(Lugar.SALA_ESPERA);
					smartFortwo.setMotorista(motoristaEmbarque);
					smartFortwo.setPassageiro(passageiroEmbarque);
					service.transportarPassageiroParaEmbarque(smartFortwo, salaEmbarque);
					break;

					default:
						break;
			}

		}

	}

}

