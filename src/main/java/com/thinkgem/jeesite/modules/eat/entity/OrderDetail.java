/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 订单Entity
 * 
 * @author ThinkGem
 * @version 2014-05-13
 */
@Entity
@Table(name = "eat_order_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderDetail extends IdEntity<OrderDetail> {

	private static final long serialVersionUID = 1L;
	private String orderId;
	private String dishesId;
	private String dishesName;
	private float dishesPrice;
	private float realPrice;
	private int num;
	private Office company;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@Length(min = 1, max = 64)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Length(min = 1, max = 64)
	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	@Length(min = 1, max = 200)
	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public float getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(float dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public float getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(float realPrice) {
		this.realPrice = realPrice;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
