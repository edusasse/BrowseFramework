package br.com.browseframework.application.db.domain;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import br.com.browseframework.base.data.dto.BaseDTO;

@javax.persistence.Entity
@Table(name = "Entity_Inheritance")
public class EntityInheritance extends BaseDTO<Long> {

	private static final long serialVersionUID = -6312808814932723768L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_entity_inheritance")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cod_entity")
	private Entity entity;
	
	@Column(name = "des_strategy", nullable = false)
	private String strategy;

	@Column(name = "ind_inheritor", nullable = false)
	private boolean inheritor;
	
	@Column(name = "des_grupo", nullable = false)
	private String grupo;
	
	@Column(name = "des_name", nullable = false)
	private String name;
	
	@Column(name = "des_value", nullable = false)
	private String value;
	
	@Version
	@Column(name = "NRO_VERSAO", nullable = false, length = 10)
	private Long versao;

	// GETTERS && SETTERS 
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public boolean isInheritor() {
		return inheritor;
	}

	public void setInheritor(boolean inheritor) {
		this.inheritor = inheritor;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
	
}