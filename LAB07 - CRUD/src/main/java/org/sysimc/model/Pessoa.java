package org.sysimc.model;

import jdk.jfr.BooleanFlag;

import java.util.Objects;

@

public class Pessoa {
//    private int id;
    private String nome;
    private Float altura;
    private Float peso;
    private Float imc;
    private String classificacao;
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // CONSTRUTOR
//    public Pessoa() {
//        this.id = ++this.id;
//    }
//
//    public Pessoa(int id, String nome, Float altura, Float peso, Float imc, String classificacao) {
//        this.nome = nome;
//        this.altura = altura;
//        this.peso = peso;
//        this.imc = imc;
//        this.classificacao = classificacao;
//    }
//
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // GET E SET
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getNome() {
//        return nome;
//    }
//
//    public void setNome(String nome) {
//        this.nome = nome;
//    }
//
//    public Float getAltura() {
//        return altura;
//    }
//
//    public void setAltura(Float altura) {
//        this.altura = altura;
//    }
//
//    public Float getPeso() {
//        return peso;
//    }
//
//    public void setPeso(Float peso) {
//        this.peso = peso;
//    }
//
//    public Float getImc() {
//        return imc;
//    }
//
//    public void setImc(Float imc) {
//        this.imc = imc;
//    }
//
//    public String getClassificacao() {
//        return classificacao;
//    }
//
//    public void setClassificacao(String classificacao) {
//        this.classificacao = classificacao;
//    }
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // TO STRING





    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", altura=" + altura +
                ", peso=" + peso +
                ", imc=" + imc +
                ", classificacao='" + classificacao + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pessoa)) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // CALCULAR IMC
    public Float calcularIMC(){
        this.imc = this.peso/(this.altura * this.altura); //Math.pow(this.altura, 2);
        return this.imc;
    }

    // CLASSIFICAÇÃO
    public String classificacaoIMC(){
        this.imc = this.calcularIMC();

        if(this.imc < 18.5)
            this.classificacao = "Abaixo do peso";
        else if(this.imc <= 18.5 && this.imc < 24.9)
            this.classificacao = "Peso normal";
        else if(this.imc <= 24.9 && this.imc < 29.9)
            this.classificacao = "Sobrepeso";
        else if(this.imc <= 29.9 && this.imc < 34.9)
            this.classificacao = "Obesidade Grau 1";
        else if(this.imc <= 34.9 && this.imc < 39.9)
            this.classificacao = "Obesidade Grau 2";
        else
            this.classificacao = "Obesidade Grau 3";

        return this.classificacao;
    }


}
