package br.com.browseframework.application.db.domain;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import br.com.browseframework.base.data.dto.BaseDTO;

@javax.persistence.Entity
@Table(name = "Entity")
public class Entity extends BaseDTO<Long> {

	private static final long serialVersionUID = -6312808814932723768L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_ENTITY")
	private Long id;

	@Column(name = "DES_NAME", nullable = false)
	private String name;

	@Column(name = "DES_SCHEMA", nullable = false)
	private String schema;
	
	@ManyToOne
	@JoinColumn(name = "cod_extends")
	private Entity superEntity;
	
	@OneToMany(mappedBy = "entity", cascade=javax.persistence.CascadeType.ALL,  fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<EntityColumn> columns;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Entity getSuperEntity() {
		return superEntity;
	}

	public void setSuperEntity(Entity superEntity) {
		this.superEntity = superEntity;
	}

	public List<EntityColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<EntityColumn> columns) {
		this.columns = columns;
	}
	
}