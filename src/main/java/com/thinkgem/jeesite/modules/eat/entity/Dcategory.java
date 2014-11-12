/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 类别Entity
 * 
 * @author ThinkGem
 * @version 2014-05-04
 */
@Entity
@Table(name = "eat_dcategory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dcategory extends IdEntity<Dcategory> {

	private static final long serialVersionUID = 1L;
	private String name; // 名称
	private Float orderPlace;
	private String code;
	private Store store; // 归属门店

	public Dcategory() {
		super();
	}

	@ManyToOne
	@JoinColumn(name="store_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="归属门店不能为空")
	@ExcelField(title="归属门店", align=2, sort=25)
	public Store getStore() {
		return store;
	}


	public void setStore(Store store) {
		this.store = store;
	}

	@Length(min = 1, max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getOrderPlace() {
		return orderPlace;
	}

	public void setOrderPlace(Float orderPlace) {
		this.orderPlace = orderPlace;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
