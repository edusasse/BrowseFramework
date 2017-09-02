package br.com.browseframework.application.md.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframework.base.data.dto.BaseDTO;

@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "client")
public class Client extends BaseDTO<Long> {

	private static final long serialVersionUID = -8044831815378293654L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_client", length = 11)
	private Long id;

	@JoinColumn(name = "cod_person", nullable = false)
	@ManyToOne
	private Person person;

	@Column(name = "des_name", nullable = false, length = 255)
	private String name;

	@Column(name = "des_registration_number", nullable = false, length = 255)
	private String registrationNumber;

	@Column(name = "des_address", nullable = false, length = 255)
	private String address;

	@JoinColumn(name = "cod_city", nullable = false)
	@ManyToOne
	private City city;

	@Column(name = "nro_zip_code", nullable = false, length = 10)
	private String zip;

	@Column(name = "nro_telephone", nullable = false, length = 45)
	private String telephone;

	@Column(name = "des_url_nickname", nullable = false, length = 255)
	private String urlNickname;

	@Column(name = "des_database", nullable = false, length = 255)
	private String database;

	@Column(name = "des_login", nullable = true, length = 4000)
	private String login;

	@Column(name = "des_password", nullable = true, precision = 10, scale = 2)
	private String password;

	@Version
	@Column(name = "nro_version", nullable = false, length = 10)
	private Long versao;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS && SETTERS

	public String getUrlNickname() {
		return urlNickname;
	}

	public void setUrlNickname(String urlNickname) {
		this.urlNickname = urlNickname;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
}