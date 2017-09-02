package br.com.browseframework.application.md.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframework.base.data.dto.BaseDTO;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "country")
public class Country extends BaseDTO<Long> {

	private static final long serialVersionUID = -8044831815378293654L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_country", length = 11)
	private Long id;

	@Column(name = "des_country", nullable = false, length = 255)
	private String name;
	
	@Version
	@Column(name = "nro_version", nullable = false, length = 10)
	private Long version;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS && SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	} 
	 
}