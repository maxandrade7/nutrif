package br.edu.ladoss.entity.migration;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "situacaoMatricula")
@Entity
@Table(name = "tb_situacao_matricula_migration")
@NamedQuery(name = "SituacaoMatricula.getAll", query = "from SituacaoMatricula")
public class SituacaoMatricula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_situacao_matricula")
	private Integer id;
	
	@Column(name = "nm_situacao_matricula")
	private String nome;
	
	@Column(name = "nm_descricao")
	private String descricao;

	public static int ID_MATRICULADO = 1;
	
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

	@XmlElement
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return "SituacaoMatricula [id=" + id 
				+ ", nome=" + nome + "]";
	}
}