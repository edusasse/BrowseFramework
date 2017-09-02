package br.com.browseframework.base.data.type.util;

import java.util.HashMap;
import java.util.Map;

import br.com.browseframework.base.data.type.CriterionType;

public class CriterionTypeUtil {

	/**
	 * Converts the CriterionType array into a Map indexed by the CriterionType property name.
	 * @param ct
	 * @return
	 */
	public static Map<String, CriterionType> getCriterionTypeMap(CriterionType[] ct){
		Map<String, CriterionType> retorno = new HashMap<String, CriterionType>();
		
		if (ct != null){
			for (CriterionType x : ct){
				retorno.put(x.getPropertyName(), x);
			}
		}
		
		return retorno;
	}
}
