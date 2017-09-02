package br.com.browseframework.application.db.domain;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import br.com.browseframework.application.engine.enums.TypeReferenceEnum;
import br.com.browseframework.base.data.dto.BaseDTO;

@javax.persistence.Entity
@Table(name = "Type")
public class Type extends BaseDTO<Long> {

	private static final long serialVersionUID = -6312808814932723768L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_type")
	private Long id;

	@Column(name = "DES_NAME", nullable = false)
	private String name;
	
	@Column(name = "nro_reference", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private TypeReferenceEnum reference;
 
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

	public TypeReferenceEnum getReference() {
		return reference;
	}

	public void setReference(TypeReferenceEnum reference) {
		this.reference = reference;
	}
	
}