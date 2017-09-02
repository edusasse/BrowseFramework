package br.com.browseframework.application.db.domain;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import br.com.browseframework.application.engine.enums.EntityColumnPropertyGroupEnum;
import br.com.browseframework.application.engine.enums.EntityColumnPropertyNameEnum;
import br.com.browseframework.base.data.dto.BaseDTO;

@javax.persistence.Entity
@Table(name = "Entity_Column_Property")
public class EntityColumnProperty extends BaseDTO<Long> {

	private static final long serialVersionUID = -6312808814932723768L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_entity_column_property")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cod_entity_column")
	private EntityColumn entityColumn;
	
	@Column(name = "des_grupo", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityColumnPropertyGroupEnum grupo;
	
	@Column(name = "des_name", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityColumnPropertyNameEnum name;
	
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

	public EntityColumn getEntityColumn() {
		return entityColumn;
	}

	public void setEntityColumn(EntityColumn entityColumn) {
		this.entityColumn = entityColumn;
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

	public EntityColumnPropertyGroupEnum getGrupo() {
		return grupo;
	}

	public void setGrupo(EntityColumnPropertyGroupEnum grupo) {
		this.grupo = grupo;
	}

	public EntityColumnPropertyNameEnum getName() {
		return name;
	}

	public void setName(EntityColumnPropertyNameEnum name) {
		this.name = name;
	}
 
}