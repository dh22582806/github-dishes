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
import com.thinkgem.jeesite.modules.eat.dao.DcategoryDao;
import com.thinkgem.jeesite.modules.eat.entity.Dcategory;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 类别Service
 * 
 * @author ThinkGem
 * @version 2014-05-04
 */
@Component
@Transactional(readOnly = true)
public class DcategoryService extends BaseService {

	@Autowired
	private DcategoryDao dcategoryDao;

	public Dcategory get(String id) {
		return dcategoryDao.get(id);
	}

	public Page<Dcategory> find(Page<Dcategory> page, Dcategory dcategory) {
		DetachedCriteria dc = dcategoryDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("store", "store", JoinType.LEFT_OUTER_JOIN).createAlias(
				"store.company", "company", JoinType.LEFT_OUTER_JOIN);// 关联查询表
		// 查询条件
		if (dcategory.getStore() != null
				&& StringUtils.isNotBlank(dcategory.getStore().getId())) {
			dc.add(Restrictions.eq("store.id", dcategory.getStore().getId()));
		}
		if (StringUtils.isNotEmpty(dcategory.getName())) {
			dc.add(Restrictions.like("name", "%" + dcategory.getName() + "%"));
		}
		dc.add(dataScopeFilter(currentUser, "company", ""));// 过滤权限，按权限显示楼层列表
		dc.add(Restrictions.eq(Dcategory.FIELD_DEL_FLAG,
				Dcategory.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate"))
				.addOrder(Order.desc("store.createDate"))
				.addOrder(Order.desc("createDate"));
		return dcategoryDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Dcategory dcategory) {
		dcategoryDao.save(dcategory);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		dcategoryDao.deleteById(id);
	}

	public List<Dcategory> findAll() {
		DetachedCriteria dc = dcategoryDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("store", "store", JoinType.LEFT_OUTER_JOIN).createAlias(
				"store.company", "company", JoinType.LEFT_OUTER_JOIN);// 关联查询表
		dc.add(dataScopeFilter(user, "company", ""));// 过滤权限，按权限显示楼层列表
		dc.add(Restrictions.eq(Dcategory.FIELD_DEL_FLAG,
				Dcategory.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return dcategoryDao.find(dc);
	}

	/**
	 * 统计某个日期后更新的数据
	 * 
	 * @param updateDate
	 * @return
	 */
	public long countDateChanged(Date updateDate) {
		DetachedCriteria dc = dcategoryDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		if (updateDate != null) {
			dc.add(Restrictions.gt("updateDate", updateDate));
		}
		return dcategoryDao.count(dc);
	}

}
