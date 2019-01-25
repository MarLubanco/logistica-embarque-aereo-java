package com.embarque.embarque.enums;

public enum Cargo {

  COMISSARIA("COMISSARIA"),
  PILOTO("PILOTO"),
  OFICIAL("OFICIAL"),
  CHEFE_DE_SERVICO("CHEFE_DE_SERVICO"),
  POLICIAL("POLICIAL"),
  PRESIDIARIO("PRESIDIARIO");

  private String cargo;

  Cargo(String cargo) {
    this.cargo = cargo;
  }

  public String getNome() {
    return cargo;
  }

}
