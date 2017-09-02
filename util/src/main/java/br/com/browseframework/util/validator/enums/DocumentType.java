package br.com.browseframework.util.validator.enums;

/**
 * Common types
 * @author Eduardo
 *
 */
public enum DocumentType {
	CI("CI","**********"),
	CPF("CPF","###.###.###-##"),
	CNPJ("CNPJ","##.###.###/####-##"),
	RENAVAM("RENAVAM","##.###.###-#"),
	CHASSI("CHASSI","*****************"),
	CEP("CEP","#####-###"),
	PHONE("PHONE","+## (##) ####-####"),
	CNH("CNH","###########");
 
	private String description;
	private String mask;

	private DocumentType(String descricao, String mascara) {
		setDescription(descricao);
		setMask(mascara);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mascara) {
		this.mask = mascara;
	}
	
}
