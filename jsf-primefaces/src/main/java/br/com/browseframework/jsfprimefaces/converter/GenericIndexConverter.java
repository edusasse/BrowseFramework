package br.com.browseframework.jsfprimefaces.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.apache.log4j.Logger;

/**
 * Converts out the value by the given index.
 * 
 * @author Eduardo
 *
 */
@FacesConverter(value="genericIndexConverter")
public class GenericIndexConverter implements Converter {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(GenericIndexConverter.class);
	
	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		Object result = null;
		final SelectItem selectedItem = getSelectedItemByValue(component, value);
		if (selectedItem != null){
			result = selectedItem.getValue();
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		return value != null? value.toString() : null;
	}

	/**
	 * Retrieves the value with the given selected option.
	 * @param component
	 * @param value
	 * @return
	 */
	protected SelectItem getSelectedItemByValue(UIComponent component, Object value) {
		SelectItem result = null;
		final List<SelectItem> items = this.getSelectItems(component);
		for(SelectItem si:items){
			if(si.getValue() != null && si.getValue().toString().equals(value)){
				result = si;
				break;
			}
		}
		return result;
	}

	protected List<SelectItem> getSelectItems(UIComponent component) {
		final List<SelectItem> result = new ArrayList<SelectItem>();

		int childCount = component.getChildCount();
	    if (childCount != 0){
		    final List<UIComponent> children = component.getChildren();
			for (UIComponent child : children) {
				if (child instanceof UISelectItem) {
					this.addSelectItem((UISelectItem) child, result);
				} else if (child instanceof UISelectItems) {
					this.addSelectItems((UISelectItems) child, result);
				}
			}
	    }
		return result;
	}

	protected void addSelectItem(UISelectItem uiItem, List<SelectItem> items) {
		final boolean isRendered = uiItem.isRendered();
		if (!isRendered) {
			items.add(null);
			return;
		}

		final Object value = uiItem.getValue();
		SelectItem item;

		if (value instanceof SelectItem) {
			item = (SelectItem) value;
		} else {
			Object itemValue = uiItem.getItemValue();
			String itemLabel = uiItem.getItemLabel();
			// JSF throws a null pointer exception for null values and labels,
			// which is a serious problem at design-time.
			item = new SelectItem(itemValue == null ? "" : itemValue,
					itemLabel == null ? "" : itemLabel, uiItem
							.getItemDescription(), uiItem.isItemDisabled());
		}

		items.add(item);
	}

	@SuppressWarnings("unchecked")
	protected void addSelectItems(UISelectItems uiItems, List<SelectItem> items) {

		boolean isRendered = uiItems.isRendered();
		if (!isRendered) {
			items.add(null);
			return;
		}

		Object value = uiItems.getValue();
		if (value instanceof SelectItem) {
			items.add((SelectItem) value);
		} else if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			for (int i = 0; i < array.length; i++) {
				if (array[i] instanceof SelectItemGroup) {
					resolveAndAddItems((SelectItemGroup) array[i], items);
				} else {
					items.add((SelectItem) array[i]);
				}
			}
		} else if (value instanceof Collection) {
			Iterator<SelectItem> iter = ((Collection<SelectItem>) value)
					.iterator();
			SelectItem item;
			while (iter.hasNext()) {
				item = iter.next();
				if (item instanceof SelectItemGroup) {
					resolveAndAddItems((SelectItemGroup) item, items);
				} else {
					items.add(item);
				}
			}
		} else if (value instanceof Map) {
			for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
				Object label = entry.getKey();
				SelectItem item = new SelectItem(entry.getValue(),
						label == null ? (String) null : label.toString());
				// TODO test - this section is untested
				if (item instanceof SelectItemGroup) {
					resolveAndAddItems((SelectItemGroup) item, items);
				} else {
					items.add(item);
				}
			}
		}
	}

	protected void resolveAndAddItems(SelectItemGroup group, List<SelectItem> items) {
		for (SelectItem item : group.getSelectItems()) {
			if (item instanceof SelectItemGroup) {
				resolveAndAddItems((SelectItemGroup) item, items);
			} else {
				items.add(item);
			}
		}
	}

}