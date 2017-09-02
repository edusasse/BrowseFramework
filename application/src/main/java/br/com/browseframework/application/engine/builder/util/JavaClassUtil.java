package br.com.browseframework.application.engine.builder.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.browseframework.application.engine.exception.BrowseApplicationException;

/**
 * Java Class build util methods.
 * @author Eduardo
 *
 */
public class JavaClassUtil {

	private final static String expression = "([a-z]|[\\$]|[\\_]){1}[A-Za-z0-9\\$\\_]*";
	private final static Pattern pattern = Pattern.compile(expression,	Pattern.CANON_EQ);

	/**
	 * Removes the underlines and transforms the subsequent character to upper case 
	 * @param tableName
	 * @return
	 */
	public static String getClassNameFromTableName(String tableName) {
		final StringBuilder result = new StringBuilder(tableName.length());
		final String[] words = tableName.split("\\_");
		for(int i=0,l=words.length;i<l;++i) {
		  result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
		}
		return result.toString();
	}
	
	/**
	 * Removes the underlines and transforms the subsequent character to upper case.
	 * The field will always start with a lowercase character.
	 * @param columnName
	 * @return
	 */
	public static String getFieldNameFromTableColumnName(String columnName) throws BrowseApplicationException {
		String result = null;
		
		final StringBuilder aux = new StringBuilder(columnName.length());
		final String[] words = columnName.split("\\_");
		for(int i=0,l=words.length;i<l;++i) {
			if (words[i].trim().length() != 0){
				aux.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
			}
		}
		
		result = aux.toString();
		if (result.length() > 1){
			result = aux.substring(0, 1).toLowerCase() + result.substring(1);			
		} else {
			result = result.toLowerCase();
		}
		
		if (!isValidFieldName(result)){
			final String original = result; // bkp
			result = "_" + result;
			if (!isValidFieldName(result)){
				throw new BrowseApplicationException("Not able to determine a valid field name for the column [" + columnName + "]. Generated field name [" + original + "]");
			}
		}
		
		return result;
	}

	/**
	 * Validates field name. 
	 *  - Variable names are case-sensitive. A variable's
	 * name can be any legal identifier — an unlimited-length sequence of
	 * Unicode letters and digits, beginning with a letter, the dollar sign "$",
	 * or the underscore character "". The convention, however, is to always
	 * begin your variable names with a letter, not "$" or "". Additionally, the
	 * dollar sign character, by convention, is never used at all. You may find
	 * some situations where auto-generated names will contain the dollar sign,
	 * but your variable names should always avoid using it. A similar
	 * convention exists for the underscore character; while it's technically
	 * legal to begin your variable's name with "_", this practice is
	 * discouraged. White space is not permitted. 
	 *  - Subsequent characters may be
	 * letters, digits, dollar signs, or underscore characters. Conventions (and
	 * common sense) apply to this rule as well. When choosing a name for your
	 * variables, use full words instead of cryptic abbreviations. Doing so will
	 * make your code easier to read and understand. In many cases it will also
	 * make your code self-documenting; fields named cadence, speed, and gear,
	 * for example, are much more intuitive than abbreviated versions, such as
	 * s, c, and g. Also keep in mind that the name you choose must not be a
	 * keyword or reserved word.
	 * 
	 * @param fieldName
	 * @return
	 */
	public static boolean isValidFieldName(String fieldName) {
		boolean result = false;
		final CharSequence inputStr = fieldName;
		final Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			result = true;
		}
		return result;
	}
}
