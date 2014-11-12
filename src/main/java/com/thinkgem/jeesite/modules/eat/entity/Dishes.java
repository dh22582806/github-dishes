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
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 菜品Entity
 * 
 * @author ThinkGem
 * @version 2014-05-02
 */
@Entity
@Table(name = "eat_dishes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dishes extends IdEntity<Dishes> {

	private static final long serialVersionUID = 1L;
	private String name; // 名称
	private Float price;
	private String pic;
	private Dcategory dcategory;// 归属菜品类别
	private String nameCode;//品码
	private String isCurPrice;//是否时价
	private String isDiscount;//是否可折扣
	private String isMinChange;//是否计入低消

	// private Dcategory dCategory;

	public Dishes() {
		super();
	}

	
	@ManyToOne
	@JoinColumn(name="dcategory_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="菜品类别不能为空")
	@ExcelField(title="菜品类别", align=2, sort=25)
	public Dcategory getDcategory() {
		return dcategory;
	}

	public void setDcategory(Dcategory dcategory) {
		this.dcategory = dcategory;
	}

	@Length(min = 1, max = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull(message="菜品价格不能为空")
	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Length(min = 1, max = 2000)
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	@NotNull(message="品码不能为空")
	@Length(min = 1, max = 20)
	public String getNameCode() {
		return nameCode;
	}


	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	@NotNull(message="是否时价不能为空")
	@Length(min = 1, max = 1)
	public String getIsCurPrice() {
		return isCurPrice;
	}


	public void setIsCurPrice(String isCurPrice) {
		this.isCurPrice = isCurPrice;
	}

	@NotNull(message="是否可折扣不能为空")
	@Length(min = 1, max = 1)
	public String getIsDiscount() {
		return isDiscount;
	}


	public void setIsDiscount(String isDiscount) {
		this.isDiscount = isDiscount;
	}

	@NotNull(message="是否计入低消不能为空")
	@Length(min = 1, max = 1)
	public String getIsMinChange() {
		return isMinChange;
	}


	public void setIsMinChange(String isMinChange) {
		this.isMinChange = isMinChange;
	}

}
