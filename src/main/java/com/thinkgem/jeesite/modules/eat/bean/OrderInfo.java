package com.thinkgem.jeesite.modules.eat.bean;

import java.util.List;

import com.thinkgem.jeesite.modules.eat.entity.Order;
import com.thinkgem.jeesite.modules.eat.entity.OrderDetail;

public class OrderInfo {
	private Order order;
	private List<OrderDetail> orderDetails;
	private List<OrderInfo> childList;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public List<OrderInfo> getChildList() {
		return childList;
	}

	public void setChildList(List<OrderInfo> childList) {
		this.childList = childList;
	}

}
