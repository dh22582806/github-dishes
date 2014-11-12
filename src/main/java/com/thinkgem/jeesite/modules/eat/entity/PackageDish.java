/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.IdEntity;

/**
 * 套餐明细Entity
 * @author wjh
 * @version 2014-11-12
 */
@Entity
@Table(name = "eat_packageDish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PackageDish extends IdEntity<PackageDish> {
	
	private static final long serialVersionUID = 1L;
	private String id; 		// 编号
	private String name; 	// 名称

	public PackageDish() {
		super();
	}

	public PackageDish(String id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_eat_packageDish")
	//@SequenceGenerator(name = "seq_eat_packageDish", sequenceName = "seq_eat_packageDish")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Length(min=1, max=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}


