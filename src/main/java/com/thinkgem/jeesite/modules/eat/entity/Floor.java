/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
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

/**
 * 楼层Entity
 * @author wjh
 * @version 2014-11-04
 */
@Entity
@Table(name = "eat_floor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Floor extends IdEntity<Floor> {
	
	private static final long serialVersionUID = 1L;
	private String name; 	// 名称
	private Store store;//所属门店

	public Floor() {
		super();
	}


	@Length(min=1, max=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
}


