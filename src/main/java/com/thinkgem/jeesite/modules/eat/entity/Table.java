/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * 桌台Entity
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Entity
@javax.persistence.Table(name = "eat_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Table extends IdEntity<Table> {

	private static final long serialVersionUID = 1L;
	private String name; // 编号
	private Room room;

	public Table() {
		super();
	}

	@Length(min = 1, max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "room_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message = "归属房间不能为空")
	@ExcelField(title = "归属房间", align = 2, sort = 25)
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
