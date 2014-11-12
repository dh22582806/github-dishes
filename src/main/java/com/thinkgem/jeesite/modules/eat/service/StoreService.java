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
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.dao.StoreDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 门店Service
 * @author wjh
 * @version 2014-11-05
 */
@Component
@Transactional(readOnly = true)
public class StoreService extends BaseService {

	@Autowired
	private StoreDao storeDao;
	
	public Store get(String id) {
		return storeDao.get(id);
	}
	
	public Page<Store> find(Page<Store> page, Store store) {
		DetachedCriteria dc = storeDao.createDetachedCriteria();
		
		User currentUser = UserUtils.getUser();
		dc.createAlias("company", "company");//关联查询表
		//查询条件
		if (store.getCompany()!=null && StringUtils.isNotBlank(store.getCompany().getId())){
			dc.add(Restrictions.or(
					Restrictions.eq("company.id", store.getCompany().getId()),
					Restrictions.like("company.parentIds", "%,"+store.getCompany().getId()+",%")
					));
		}
		dc.add(dataScopeFilter(currentUser, "company", ""));//过滤权限，按权限显示门店列表
		if (StringUtils.isNotEmpty(store.getName())){
			dc.add(Restrictions.like("name", "%"+store.getName()+"%"));
		}
		dc.add(Restrictions.eq(Store.FIELD_DEL_FLAG, Store.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return storeDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Store store) {
		storeDao.save(store);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		storeDao.deleteById(id);
	}
	
}
