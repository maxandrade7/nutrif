package br.edu.ladoss.entity.migration;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "periodoRefeicaoRealizada")
public class PeriodoRefeicaoRealizada {

	private Refeicao refeicao;

	private Date dataInicio;
	
	private Date dataFim;

	@XmlElement
	public Refeicao getRefeicao() {
		return refeicao;
	}

	public void setRefeicao(Refeicao refeicao) {
		this.refeicao = refeicao;
	}
	
	@XmlElement
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@XmlElement
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	@Override
	public String toString() {
		return "PeriodoRefeicaoRealizada [refeicao=" + refeicao
				+ ", dataInicio=" + dataInicio 
				+ ", dataFim=" + dataFim + "]";
	}
}
