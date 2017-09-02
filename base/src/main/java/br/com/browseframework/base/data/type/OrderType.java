package br.com.browseframework.base.data.type;

import br.com.browseframework.base.data.enums.OrderDirection;

public interface OrderType extends CriterionType {

	public abstract OrderDirection getOrderDirection();

	public abstract void setOrderDirection(OrderDirection orderDirection);

}