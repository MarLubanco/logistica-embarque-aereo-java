package com.embarque.embarque;

import com.embarque.embarque.enums.ECommand;
import com.embarque.embarque.enums.Lugar;
import com.embarque.embarque.exception.TransporteLotadoException;
import com.embarque.embarque.exception.TripulacaoIncorretaException;
import com.embarque.embarque.model.Pessoa;
import com.embarque.embarque.model.SmartFortwo;
import com.embarque.embarque.service.Comandos;
import com.embarque.embarque.service.SmartFortwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class EmbarqueApplication {

	public static void main(String[] args) throws TransporteLotadoException, TripulacaoIncorretaException {
		SpringApplication.run(EmbarqueApplication.class, args);

		SmartFortwoService service = new SmartFortwoService();
		SmartFortwo smartFortwo = new SmartFortwo();
		while (true) {
			System.out.println("1 - TRANSPORTAR ");
			System.out.println("2 - DESEMBARCAR ");
			System.out.println("3 - EMBARCAR ");
			Scanner scanner = new Scanner(System.in);
			String comando = scanner.nextLine();

			switch (comando) {
				case "TRANSPORTAR":
					System.out.println("Cargo da pessoa motorista: ");
					String cargoMotorista = scanner.next();
					Pessoa motorista = new Pessoa(cargoMotorista);
					System.out.println("Cargo da pessoa passageiro: ");
					String cargoPassageiro = scanner.next();
					Pessoa passageiro = new Pessoa(cargoPassageiro);
					smartFortwo.setLugar(Lugar.SALA_ESPERA);
					smartFortwo.setMotorista(motorista);
					smartFortwo.setPassageiro(passageiro);
					service.transportarPassageiro(smartFortwo);
			}
//
//			Comandos.apply();
//			Scanner scanner = new Scanner(System.in);
//			scanner.next();
		}

	}

}

