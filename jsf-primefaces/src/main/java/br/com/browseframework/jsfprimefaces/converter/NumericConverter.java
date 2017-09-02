package br.com.browseframework.jsfprimefaces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class NumericConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		Long result = null;
		
		if (valor != null){
			String aux = "";
			for (Character c : valor.toCharArray()){
				if (c.equals('0')
						|| c.equals('1')
						|| c.equals('2')
						|| c.equals('3')
						|| c.equals('4')
						|| c.equals('5')
						|| c.equals('6')
						|| c.equals('7')
						|| c.equals('8')
						|| c.equals('9')){
					aux += c;
				}
			}
			
			result = Long.parseLong(aux);
		}
		
		return result;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object valor) {
		return valor.toString();
	}
}