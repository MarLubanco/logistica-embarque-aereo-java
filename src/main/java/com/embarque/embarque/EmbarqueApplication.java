package com.embarque.embarque;

import com.embarque.embarque.enums.ECommand;
import com.embarque.embarque.service.Comandos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class EmbarqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbarqueApplication.class, args);

		while (true) {
			Comandos.apply();
			Scanner scanner = new Scanner(System.in);
			scanner.next();
		}

	}

}

