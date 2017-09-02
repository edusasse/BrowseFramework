package br.com.browseframework.base.data;

import br.com.browseframework.base.data.enums.OrderDirection;
import br.com.browseframework.base.data.type.OrderType;

/**
 * Order is used to transport the JPA ordering criterion
 * @author Eduardo
 *
 */
public class Order extends Criterion implements OrderType {
	// The order direction for the order by clasuse on the property
	private OrderDirection orderDirection;

	// GETTERS && SETTERS
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.OrderType#getOrderDirection()
	 */
	@Override
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.OrderType#setOrderDirection(br.com.browseframework.data.enums.OrderDirection)
	 */
	@Override
	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}
}
