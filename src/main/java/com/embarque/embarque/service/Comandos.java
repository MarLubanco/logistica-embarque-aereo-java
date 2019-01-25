package com.embarque.embarque.service;

import com.embarque.embarque.enums.ECommand;

import java.util.HashMap;
import java.util.Map;

public class Comandos {

  static Map<ECommand, String> availableCommands = new HashMap<>();

  public static void comandos() {
    availableCommands.put(ECommand.DESEMBARCAR_AVIAO, "Mostra a lista de comandos disponíveis.");
    availableCommands.put(ECommand.DESEMBARCAR_SALA_ESPERA, "Configura o ambiente um novo ambiente virtual.");
    availableCommands.put(ECommand.EMBARCAR_AVIAO, "Cria um novo processo no sistema virtual.");
    availableCommands.put(ECommand.EMBARCAR_SALA_ESPERA, "Simula o acesso à memória do processo.");
    availableCommands.put(ECommand.TRANSPORTAR_PASSAGEIROS, "Limpa totalmente o sistema virtual.");
  }

  public static boolean apply() {
    comandos();
    System.out.println("Comandos disponíveis:");
    availableCommands.forEach((key, value) -> System.out.println(key + " - " + value));
    return true;
  }
}
