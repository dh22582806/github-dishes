/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.eat.entity.PackageDish;
import com.thinkgem.jeesite.modules.eat.dao.PackageDishDao;

/**
 * 套餐明细Service
 * @author wjh
 * @version 2014-11-12
 */
@Component
@Transactional(readOnly = true)
public class PackageDishService extends BaseService {

	@Autowired
	private PackageDishDao packageDishDao;
	
	public PackageDish get(String id) {
		return packageDishDao.get(id);
	}
	
	public Page<PackageDish> find(Page<PackageDish> page, PackageDish packageDish) {
		DetachedCriteria dc = packageDishDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(packageDish.getName())){
			dc.add(Restrictions.like("name", "%"+packageDish.getName()+"%"));
		}
		dc.add(Restrictions.eq(PackageDish.FIELD_DEL_FLAG, PackageDish.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return packageDishDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(PackageDish packageDish) {
		packageDishDao.save(packageDish);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		packageDishDao.deleteById(id);
	}
	
}
