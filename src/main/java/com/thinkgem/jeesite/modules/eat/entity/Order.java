/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 订单Entity
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Entity
@javax.persistence.Table(name = "eat_order")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order extends IdEntity<Order> {

	private static final long serialVersionUID = 1L;
	private Office company;
	// private Table table;
	private String tableId;
	private int state;
	private float money;
	private String parentId;

	// private List<Order> childList;

	public Order() {
		super();
	}

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

	// @ManyToOne
	// @JoinColumn(name = "table_id")
	// @NotFound(action = NotFoundAction.IGNORE)
	// public Table getTable() {
	// return table;
	// }
	//
	// public void setTable(Table table) {
	// this.table = table;
	// }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	// @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
	// CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "parentId")
	// @Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
	// @OrderBy(value = "sort")
	// @NotFound(action = NotFoundAction.IGNORE)
	// public List<Order> getChildList() {
	// return childList;
	// }
	//
	// public void setChildList(List<Order> childList) {
	// this.childList = childList;
	// }

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

}
