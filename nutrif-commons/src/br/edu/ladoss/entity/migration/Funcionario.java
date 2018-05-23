package br.edu.ladoss.entity.migration;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "funcionario")
@Entity
@Table(name = "tb_funcionario_migration")
@NamedQuery(name = "Funcionario.getAll", query = "from Funcionario")
@DiscriminatorValue(value = "1")
public class Funcionario extends Pessoa {

	private static final long serialVersionUID = 7914104914276090901L;

	public Funcionario() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_funcionario")
	private Integer id;
	
	@Column(name = "nr_siape", length = 13, unique = true)
	private String siape;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_id_setor")
	private Setor setor;

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlElement
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	public static Funcionario getFuncionario(Pessoa pessoa) {
		
		Funcionario funcionario = new Funcionario();
		
		funcionario.setId(pessoa.getId());
		funcionario.setNome(pessoa.getNome());
		funcionario.setEmail(pessoa.getEmail());
		funcionario.setCampus(pessoa.getCampus());
		funcionario.setSenha(pessoa.getSenha());
		funcionario.setKeyAuth(pessoa.getKeyAuth());
		funcionario.setRoles(pessoa.getRoles());
		funcionario.setAtivo(pessoa.isAtivo());		
		funcionario.setTipo(pessoa.getTipo());
		
		return funcionario;		
	}
	
	@Override
	public String toString() {
		return "Funcionario [" + super.toString() + "]";
	}
}