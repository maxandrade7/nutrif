package br.edu.nutrif.entitys;

public class Curso {

	private Integer id;

	public Curso(Integer id) {
		this.id = id;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	private String nome;
}