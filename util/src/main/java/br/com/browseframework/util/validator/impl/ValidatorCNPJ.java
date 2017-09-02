package br.com.browseframework.util.validator.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.util.validator.Validator;

public class ValidatorCNPJ implements Validator {

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#validator(java.lang.String)
	 */
	@Override
	public boolean validator(String numero) {
		if(numero != null){
			try{
				Long.parseLong(getAbsolutValue(numero));
			}catch(Exception e){
				throw new GenericBusinessException("CNPJ inválido["+numero+"]");
			}
		}
		

		while (numero.length() < 14){
			numero = "0"+numero;
		}
		
		if (numero.trim().length() != 14){
			// Formatador com uma mascara rasa
			NumberFormat nf = new DecimalFormat("#0");
			// número de digitos desejado
			nf.setMinimumIntegerDigits(14);
			numero = nf.format(Long.parseLong(numero));
		}
		
		String CNPJ = numero;
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
				|| CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333")
				|| CNPJ.equals("44444444444444")
				|| CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666")
				|| CNPJ.equals("77777777777777")
				|| CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14)){
			throw new GenericBusinessException("CNPJ inválido["+numero+"]");
		}

		int soma = 0, dig;
		String cnpj_calc = numero.substring(0, 12);

		if (numero.length() != 14)
			return false;

		char[] chr_cnpj = numero.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);

		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		return numero.equals(cnpj_calc);
	}

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#getAbsolutValue(java.lang.String)
	 */
	@Override
	public String getAbsolutValue(String numero){
		if(numero != null){
	        numero = numero.replaceAll("[^0-9]", "");
		}
		return numero;
	}
}