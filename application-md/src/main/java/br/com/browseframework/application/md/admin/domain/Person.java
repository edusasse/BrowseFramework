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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "person")
public class Person extends BaseDTO<Long> {

	private static final long serialVersionUID = -8044831815378293654L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_person", length = 11)
	private Long id;

	@Column(name = "nom_person", nullable = false, length = 80)
	private String name;

	@Column(name = "nro_telephone", nullable = true, length = 45)
	private String telephone;

	@Column(name = "des_email", nullable = false, length = 255)
	private String email;

	@Column(name = "des_password", nullable = false, length = 255)
	private String password;

	@Column(name = "des_question", nullable = false, length = 255)
	private String question;

	@Column(name = "des_answer", nullable = false, length = 255)
	private String answer;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	// GETTERS && SETTERS

}