package br.com.browseframework.application.db.domain;
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
@Table(name = "Entity_Column")
public class EntityColumn extends BaseDTO<Long> {

	private static final long serialVersionUID = -6312808814932723768L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_entity_column")
	private Long id;

	@ManyToOne(cascade=javax.persistence.CascadeType.ALL)
	@JoinColumn(name = "cod_entity")
	private Entity entity;
	
	@ManyToOne
	@JoinColumn(name = "cod_type")
	private Type type;
	
	@Column(name = "des_name", nullable = false)
	private String name;
	
	@Column(name = "ind_unique", nullable = false)
	private boolean unique = false;

	@Column(name = "ind_insertable", nullable = false)
	private boolean insertable = true;
	
	@Column(name = "ind_updatable", nullable = false)
	private boolean updatable = true;
	
	@Column(name = "val_length", nullable = false)
	private int length = 255;
	
	@Column(name = "val_precision", nullable = false)
	private int precision = 0;
	
	@Column(name = "val_scale", nullable = false)
	private int scale = 0;
	
	@Column(name = "ind_id", nullable = false)
	private boolean identity = false;
		
	@Column(name = "ind_enumeration", nullable = false, length=1)
	private boolean enumeration;
	
	@OneToMany(mappedBy = "entityColumn", cascade=javax.persistence.CascadeType.ALL,  fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<EntityColumnProperty> properties;
	
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isIdentity() {
		return identity;
	}

	public void setIdentity(boolean identity) {
		this.identity = identity;
	}

	public boolean isEnumeration() {
		return enumeration;
	}

	public void setEnumeration(boolean enumeration) {
		this.enumeration = enumeration;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public List<EntityColumnProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<EntityColumnProperty> properties) {
		this.properties = properties;
	}
	
}