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
import com.thinkgem.jeesite.modules.eat.dao.RoomDao;
import com.thinkgem.jeesite.modules.eat.entity.Dcategory;
import com.thinkgem.jeesite.modules.eat.entity.Room;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 房间Service
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Component
@Transactional(readOnly = true)
public class RoomService extends BaseService {

	@Autowired
	private RoomDao roomDao;

	public Room get(String id) {
		return roomDao.get(id);
	}

	public Page<Room> find(Page<Room> page, Room room) {
		DetachedCriteria dc = roomDao.createDetachedCriteria();
		// 查询条件
		if (room.getFloor()!= null
				&& StringUtils.isNotBlank(room.getFloor().getId())) {
			dc.add(Restrictions.eq("floor.id", room.getFloor().getId()));
		}
		if (StringUtils.isNotEmpty(room.getName())) {
			dc.add(Restrictions.like("name", "%" + room.getName() + "%"));
		}
		User currentUser = UserUtils.getUser();
		dc.createAlias("floor", "floor", JoinType.LEFT_OUTER_JOIN)
				.createAlias("floor.store", "store", JoinType.LEFT_OUTER_JOIN)
				.createAlias("floor.store.company", "company",
						JoinType.LEFT_OUTER_JOIN);
		dc.add(dataScopeFilter(currentUser, "company", ""));// 过滤权限
		dc.add(Restrictions.eq(Room.FIELD_DEL_FLAG, Room.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("company.createDate"))
				.addOrder(Order.desc("store.createDate"))
				.addOrder(Order.desc("floor.createDate"))
				.addOrder(Order.desc("createDate"));
		return roomDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Room room) {
		roomDao.save(room);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		roomDao.deleteById(id);
	}

	public List<Room> findAll() {
		DetachedCriteria dc = roomDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		dc.add(Restrictions.eq(Dcategory.FIELD_DEL_FLAG,
				Dcategory.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return roomDao.find(dc);
	}

	public Long countDateChanged(Date updateDate) {
		DetachedCriteria dc = roomDao.createDetachedCriteria();
		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		if (updateDate != null) {
			dc.add(Restrictions.gt("updateDate", updateDate));
		}
		return roomDao.count(dc);
	}

}
