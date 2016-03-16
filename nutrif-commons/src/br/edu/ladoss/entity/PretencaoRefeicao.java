package br.edu.ladoss.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pretencaoRefeicao")
@Entity
@Table(name = "tb_pretencao_refeicao")
@NamedQuery(name = "PretencaoRefeicao.getAll", query = "from PretencaoRefeicao")
public class PretencaoRefeicao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pretencao_refeicao")
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_id_dia_refeicao", referencedColumnName="id_dia_refeicao")
	private DiaRefeicao diaRefeicao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_requisicao", insertable = true, updatable = false)
	private Date dataHoraRequisicao;

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public DiaRefeicao getDiaRefeicao() {
		return diaRefeicao;
	}

	public void setDiaRefeicao(DiaRefeicao diaRefeicao) {
		this.diaRefeicao = diaRefeicao;
	}

	@XmlElement
	public Date getDataHoraRequisicao() {
		return dataHoraRequisicao;
	}

	public void setDataHoraRequisicao(Date dataRequisicao) {
		this.dataHoraRequisicao = dataRequisicao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PretencaoRefeicao[id=" + id + " diaRefeicao=" + diaRefeicao 
				+ " dataHoraPretencao=" + dataHoraRequisicao + "]";
	}
}
