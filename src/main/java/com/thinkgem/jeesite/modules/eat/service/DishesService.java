/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.eat.dao.DishesDao;
import com.thinkgem.jeesite.modules.eat.entity.Dishes;
import com.thinkgem.jeesite.modules.eat.exception.BusinessException;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 菜品Service
 * 
 * @author ThinkGem
 * @version 2014-05-02
 */
@Component
@Transactional(readOnly = true)
public class DishesService extends BaseService {

	@Autowired
	private DishesDao dishesDao;

	public Dishes get(String id) {
		return dishesDao.get(id);
	}

	public List<Dishes> findAll() {
		DetachedCriteria dc = dishesDao.createDetachedCriteria();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(UserUtils.getUser(), "company", "createBy"));
		dc.add(Restrictions.eq(Dishes.FIELD_DEL_FLAG, Dishes.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return dishesDao.find(dc);
	}

	public List<Dishes> findByCategoryId(String categoryId,
			boolean isDataScopeFilter) {
		DetachedCriteria dc = dishesDao.createDetachedCriteria();
		if (isDataScopeFilter) {
			dc.createAlias("company", "company");
			dc.add(dataScopeFilter(UserUtils.getUser(), "company", "createBy"));
		}
		dc.add(Restrictions.eq("dcategoryId", categoryId));
		dc.add(Restrictions.eq(Dishes.FIELD_DEL_FLAG, Dishes.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return dishesDao.find(dc);
	}

	public Page<Dishes> find(Page<Dishes> page, Dishes dishes,
			boolean isDataScopeFilter) {
		DetachedCriteria dc = dishesDao.createDetachedCriteria();
		dc.createAlias("dcategory", "dcategory", JoinType.LEFT_OUTER_JOIN)
				.createAlias("dcategory.store", "store",
						JoinType.LEFT_OUTER_JOIN)
				.createAlias("dcategory.store.company", "company",
						JoinType.LEFT_OUTER_JOIN);// 关联查询表
		// 查询条件
		if (dishes.getDcategory() != null
				&& StringUtils.isNotBlank(dishes.getDcategory().getId())) {
			dc.add(Restrictions.eq("dcategory.id", dishes.getDcategory()
					.getId()));
		}
		if (StringUtils.isNotEmpty(dishes.getName())) {
			dc.add(Restrictions.like("name", "%" + dishes.getName() + "%"));
		}
		dc.add(dataScopeFilter(UserUtils.getUser(), "company", ""));

		dc.add(Restrictions.eq(Dishes.FIELD_DEL_FLAG, Dishes.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate"))
				.addOrder(Order.desc("company.createDate"))
				.addOrder(Order.desc("store.createDate"))
				.addOrder(Order.desc("dcategory.createDate"))
				.addOrder(Order.desc("createDate"));
		return dishesDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Dishes dishes) {
		dishesDao.save(dishes);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		dishesDao.deleteById(id);
	}

	public Long countDateChanged(Date updateTime) {
		DetachedCriteria dc = dishesDao.createDetachedCriteria();

		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(UserUtils.getUser(), "company", "createBy"));

		if (updateTime != null) {
			dc.add(Restrictions.gt("updateDate", updateTime));
		}
		return dishesDao.count(dc);
	}

}
