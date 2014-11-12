/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

import org.hibernate.FetchMode;
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
import com.thinkgem.jeesite.modules.eat.entity.Floor;
import com.thinkgem.jeesite.modules.eat.dao.FloorDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 楼层Service
 * 
 * @author wjh
 * @version 2014-11-04
 */
@Component
@Transactional(readOnly = true)
public class FloorService extends BaseService {

	@Autowired
	private FloorDao floorDao;

	public Floor get(String id) {
		return floorDao.get(id);
	}

	public Page<Floor> find(Page<Floor> page, Floor floor) {
		DetachedCriteria dc = floorDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("store", "store",
				JoinType.LEFT_OUTER_JOIN);// 关联查询表
		dc.createAlias("store.company", "company", JoinType.LEFT_OUTER_JOIN);
		// 查询条件
		if (floor.getStore() != null
				&& StringUtils.isNotBlank(floor.getStore().getId())) {
			dc.add(Restrictions.eq("store.id", floor.getStore().getId()));
		}

		dc.add(dataScopeFilter(currentUser, "company", ""));// 过滤权限，按权限显示楼层列表
		if (StringUtils.isNotEmpty(floor.getName())) {
			dc.add(Restrictions.like("name", "%" + floor.getName() + "%"));
		}
		dc.add(Restrictions.eq(Floor.FIELD_DEL_FLAG, Floor.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate")).addOrder(Order.desc("store.createDate")).addOrder(Order.desc("createDate"));
		return floorDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Floor floor) {
		floorDao.save(floor);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		floorDao.deleteById(id);
	}

}
