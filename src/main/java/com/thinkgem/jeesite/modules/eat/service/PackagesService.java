/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

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
import com.thinkgem.jeesite.modules.eat.entity.Packages;
import com.thinkgem.jeesite.modules.eat.dao.PackagesDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 套餐Service
 * 
 * @author wjh
 * @version 2014-11-12
 */
@Component
@Transactional(readOnly = true)
public class PackagesService extends BaseService {

	@Autowired
	private PackagesDao packagesDao;

	public Packages get(String id) {
		return packagesDao.get(id);
	}

	public Page<Packages> find(Page<Packages> page, Packages packages) {
		DetachedCriteria dc = packagesDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("store", "store", JoinType.LEFT_OUTER_JOIN);// 关联查询表
		dc.createAlias("store.company", "company", JoinType.LEFT_OUTER_JOIN);
		// 查询条件
		if (packages.getStore() != null
				&& StringUtils.isNotBlank(packages.getStore().getId())) {
			dc.add(Restrictions.eq("store.id", packages.getStore().getId()));
		}
		if (StringUtils.isNotEmpty(packages.getName())) {
			dc.add(Restrictions.like("name", "%" + packages.getName() + "%"));
		}
		dc.add(dataScopeFilter(currentUser, "company", ""));// 过滤权限，按权限显示套餐列表
		dc.add(Restrictions.eq(Packages.FIELD_DEL_FLAG,
				Packages.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate"))
				.addOrder(Order.desc("store.createDate"))
				.addOrder(Order.desc("createDate"));
		return packagesDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Packages packages) {
		packagesDao.save(packages);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		packagesDao.deleteById(id);
	}

}
