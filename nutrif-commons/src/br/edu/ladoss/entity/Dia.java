package br.edu.ladoss.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.edu.ladoss.data.DataEntity;

@XmlRootElement(name = "dia")
@Entity
@Table(name = "tb_dia")
@NamedQuery(name = "Dia.getAll", query = "from Dia")
public class Dia implements DataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_dia")
	private Integer id;

	@Column(name = "nm_dia")
	private String nome;

	public Dia() {
		super();
	}
	
	public Dia (Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Dia [id=" + id + ", nome=" + nome + "]";
	}
}
