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
import com.thinkgem.jeesite.modules.eat.dao.TableDao;
import com.thinkgem.jeesite.modules.eat.entity.Dishes;
import com.thinkgem.jeesite.modules.eat.entity.Table;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 桌台Service
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Component
@Transactional(readOnly = true)
public class TableService extends BaseService {

	@Autowired
	private TableDao tableDao;

	public Table get(String id) {
		return tableDao.get(id);
	}

	public Page<Table> find(Page<Table> page, Table table) {
		DetachedCriteria dc = tableDao.createDetachedCriteria();
		// 查询条件
		if (table.getRoom() != null
				&& StringUtils.isNotBlank(table.getRoom().getId())) {
			dc.add(Restrictions.eq("room.id", table.getRoom().getId()));
		}
		if (StringUtils.isNotEmpty(table.getName())) {
			dc.add(Restrictions.like("name", "%" + table.getName() + "%"));
		}

		User currentUser = UserUtils.getUser();
		dc.createAlias("room", "room", JoinType.LEFT_OUTER_JOIN)
				.createAlias("room.floor", "floor", JoinType.LEFT_OUTER_JOIN)
				.createAlias("room.floor.store", "store",
						JoinType.LEFT_OUTER_JOIN)
				.createAlias("room.floor.store.company", "company",
						JoinType.LEFT_OUTER_JOIN);
		dc.add(dataScopeFilter(currentUser, "company", ""));// 过滤权限
		dc.add(Restrictions.eq(Table.FIELD_DEL_FLAG, Table.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate")).addOrder(Order.desc("company.createDate"))
		.addOrder(Order.desc("store.createDate"))
		.addOrder(Order.desc("floor.createDate"))
		.addOrder(Order.desc("room.createDate"))
		.addOrder(Order.desc("createDate"));
		return tableDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Table table) {
		tableDao.save(table);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		Table table = tableDao.get(id);
		table.setDelFlag(Table.DEL_FLAG_DELETE);
		tableDao.save(table);
	}

	/**
	 * 统计某个日期后更新的数据
	 * 
	 * @param updateDate
	 * @return
	 */
	public long countDateChanged(Date updateDate) {
		DetachedCriteria dc = tableDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		if (updateDate != null) {
			dc.add(Restrictions.gt("updateDate", updateDate));
		}
		return tableDao.count(dc);
	}

	public List<Table> findAll() {
		DetachedCriteria dc = tableDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		dc.add(Restrictions.eq(Dishes.FIELD_DEL_FLAG, Dishes.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return tableDao.find(dc);
	}

}
