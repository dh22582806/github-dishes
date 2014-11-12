/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.service;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.eat.bean.OrderInfo;
import com.thinkgem.jeesite.modules.eat.bean.Response;
import com.thinkgem.jeesite.modules.eat.constant.Config;
import com.thinkgem.jeesite.modules.eat.dao.DishesDao;
import com.thinkgem.jeesite.modules.eat.dao.OrderDao;
import com.thinkgem.jeesite.modules.eat.dao.OrderDetailDao;
import com.thinkgem.jeesite.modules.eat.dao.TableDao;
import com.thinkgem.jeesite.modules.eat.entity.Dishes;
import com.thinkgem.jeesite.modules.eat.entity.Order;
import com.thinkgem.jeesite.modules.eat.entity.OrderDetail;
import com.thinkgem.jeesite.modules.eat.entity.Table;
import com.thinkgem.jeesite.modules.eat.exception.BusinessException;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 订单Service
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Component
@Transactional(readOnly = true)
public class OrderService extends BaseService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private TableDao tableDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private DishesDao dishesDao;

	public Order get(String id) {
		return orderDao.get(id);
	}

	public Page<Order> find(Page<Order> page, Order order) {
		DetachedCriteria dc = orderDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		dc.addOrder(org.hibernate.criterion.Order.desc("id"));
		return orderDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Order order) {
		orderDao.save(order);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		orderDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	/**public Order openTabble(String tableId) {
		Table table = tableDao.get(tableId);
		if (table == null) {
			throw new BusinessException("桌台不存在");
		}
		// Room room = table.getRoom();
		// if (room == null) {
		// throw new BusinessException();
		// }
		User user = UserUtils.getUser();
		if (!user.getCompany().getId().equals(table.getCompany().getId())) {
			throw new BusinessException();
		}

		DetachedCriteria dc = orderDao.createDetachedCriteria();
		// dc.createAlias("table", "table");
		Junction junction = Restrictions.disjunction();
		junction.add(Restrictions.eq("tableId", tableId));
		dc.add(junction);
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		dc.add(Restrictions.in("state", Config.STATES_BUSY));
		long count = orderDao.count(dc);
		if (count > 0) {
			throw new BusinessException("此台位目前不允许入座");
		}
		Order order = new Order();
		order.setCompany(user.getCompany());
		order.setTableId(table.getId());
		order.setState(Config.STATE_OPEN_TABLE);
		orderDao.save(order);
		return order;
	}*/

	public Object getTableStatus() {
		return orderDao.getResult();
	}

	/**
	 * 取得当前正在进行中的订单
	 * 
	 * @return
	 */
	public Object getOrders() {
		DetachedCriteria dc = orderDao.createDetachedCriteria();
		Junction junction = Restrictions.disjunction();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(UserUtils.getUser(), "company", "createBy"));
		dc.add(junction);
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		dc.add(Restrictions.in("state", Config.STATES_BUSY));
		return orderDao.find(dc);
	}

	public Object getOrderInfoById(String orderId) {
		Order order = orderDao.get(orderId);
		if (order == null || !Order.DEL_FLAG_NORMAL.equals(order.getDelFlag())) {
			throw new BusinessException("订单不存在");
		}
		User user = UserUtils.getUser();
		if (!user.getCompany().getId().equals(order.getCompany().getId())) {
			throw new BusinessException();
		}

		return getOrderInfo(order);
	}

	private OrderInfo getOrderInfo(Order order) {
		DetachedCriteria dc = orderDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("orderId", order.getId()));
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		List<OrderDetail> orderDetails = orderDetailDao.find(dc);

		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrder(order);
		orderInfo.setOrderDetails(orderDetails);

		String parentId = order.getParentId();
		if (StringUtils.isEmpty(parentId)) {// 若是父订单
			DetachedCriteria dc2 = orderDao.createDetachedCriteria();
			dc2.add(Restrictions.eq("parentId", order.getId()));
			dc2.add(Restrictions
					.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
			List<Order> childs = orderDao.find(dc2);
			List<OrderInfo> childInfos = new LinkedList<OrderInfo>();
			for (Order child : childs) {
				OrderInfo childInfo = getOrderInfo(child);
				childInfos.add(childInfo);
			}
			orderInfo.setChildList(childInfos);
		}
		return orderInfo;
	}

	public Object getOrderInfoByTableId(String tableId) {
		DetachedCriteria dc = orderDao.createDetachedCriteria();
		dc.add(Restrictions.eq("tableId", tableId));
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		dc.add(Restrictions.in("state", Config.STATES_BUSY));

		User user = UserUtils.getUser();
		dc.createAlias("company", "company");
		dc.add(dataScopeFilter(user, "company", "createBy"));
		List<Order> orders = orderDao.find(dc);
		if (orders == null || orders.size() == 0) {// 不存在
			return null;
		} else if (orders.size() > 1) {// 出现脏数据
			throw new BusinessException();
		} else {// 只有一个订单记录为正确
			Order order = orders.get(0);
			return getOrderInfo(order);
		}
	}

	@Transactional(readOnly = false)
	public Object leaveTable(String orderId) {
		Order order = orderDao.get(orderId);
		if (order == null || !Order.DEL_FLAG_NORMAL.equals(order.getDelFlag())) {
			throw new BusinessException("订单不存在",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		User user = UserUtils.getUser();
		if (!user.getCompany().getId().equals(order.getCompany().getId())) {
			throw new BusinessException();
		}
		if (order.getState() != Config.STATE_OPEN_TABLE) {
			throw new BusinessException("不允许离台",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		order.setState(Config.STATE_LEAVE_TABLE);
		order.setUpdateBy(user);
		order.setUpdateDate(new Date());
		orderDao.save(order);
		return order;
	}

	/**@Transactional(readOnly = false)
	public Object changeTable(String orderId, String tableId) {
		Order order = orderDao.get(orderId);
		if (order == null) {
			throw new BusinessException("订单不存在",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		User user = UserUtils.getUser();
		if (!UserUtils.hasPermission(order.getCompany())) {
			throw new BusinessException();
		}
		if (order.getTableId().equals(tableId)) {
			throw new BusinessException("已经在此桌台上,无需换台");
		}

		Table table = tableDao.get(tableId);
		if (table == null || !Table.DEL_FLAG_NORMAL.equals(table.getDelFlag())) {
			throw new BusinessException("桌台不存在");
		}
		if (!UserUtils.hasPermission(table.getCompany())) {
			throw new BusinessException();
		}

		DetachedCriteria dc = orderDao.createDetachedCriteria();
		dc.add(Restrictions.eq("tableId", tableId));
		dc.add(Restrictions.eq(Order.FIELD_DEL_FLAG, Order.DEL_FLAG_NORMAL));
		dc.add(Restrictions.in("state", Config.STATES_BUSY));
		long count = orderDao.count(dc);
		if (count > 0) {// 查询到有空闲的桌子
			throw new BusinessException("新的桌台上已有客户");
		}

		order.setTableId(tableId);
		order.setUpdateBy(user);
		order.setUpdateDate(new Date());
		orderDao.save(order);
		return order;
	}*/

	/**
	 * 结账
	 * 
	 * @param orderId
	 * @param tableId
	 * @return
	 */
	@Transactional(readOnly = false)
	public Object chargeOrder(String orderId) {
		Order order = orderDao.get(orderId);
		if (order == null) {
			throw new BusinessException("订单不存在",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		if (!StringUtils.isEmpty(order.getParentId())) {
			throw new BusinessException("子订单不允许单独结账");
		}
		User user = UserUtils.getUser();
		if (!UserUtils.hasPermission(order.getCompany())) {
			throw new BusinessException();
		}
		order.setState(Config.STATE_CHARGED);// 处于已经结账的状态
		order.setUpdateBy(user);
		order.setUpdateDate(new Date());
		orderDao.save(order);
		return order;
	}

	/**
	 * 下订单
	 * 
	 * @param orderId
	 * @param orderDetails
	 * @return
	 */
	@Transactional(readOnly = false)
	public Object openOrder(String orderId, String[] orderDetails) {
		Order order = orderDao.get(orderId);
		if (order == null || !Order.DEL_FLAG_NORMAL.equals(order.getDelFlag())) {
			throw new BusinessException("订单不存在",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		User user = UserUtils.getUser();
		if (!UserUtils.hasPermission(order.getCompany())) {
			throw new BusinessException();
		}
		if (!StringUtils.isEmpty(order.getParentId())) {// 子订单不允许下单
			throw new BusinessException("子订单不允许下单");
		}
		switch (order.getState()) {
		case Config.STATE_OPEN_TABLE:// 开台的状态允许下单
			break;
		case Config.STATE_LEAVE_TABLE:
			throw new BusinessException("目前未在任何桌台上，不允许下单",
					Response.CODE_NEEDREFRESH_ORDER, order);
		case Config.STATE_CHARGED:
			throw new BusinessException("已经结账不允许下单",
					Response.CODE_NEEDREFRESH_ORDER, order);
		default:
			throw new BusinessException("已经下单,若要加菜，请用追单功能",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}

		if (orderDetails == null || orderDetails.length == 0) {
			throw new BusinessException("请选择需要下单的菜品");
		}
		List<OrderDetail> orderDetailsList = new LinkedList<OrderDetail>();
		OrderDetail orderDetail;
		Set<String> notExists = new HashSet<String>();

		float money = 0;
		for (String orderInfo : orderDetails) {
			String[] dets = orderInfo.split(":");
			if (dets == null || dets.length == 0) {
				continue;
			}
			int num = 1;// 数量默认为1
			if (dets.length > 1) {
				try {
					num = Integer.parseInt(dets[1]);
				} catch (Exception e) {
					num = 0;// 传值错误则不下单
				}
			}
			if (num < 1) {// 数量小于1则不下单
				continue;
			}
			String dishesId = dets[0];
			Dishes dishes = dishesDao.get(dishesId);
			if (dishes == null
					|| !Dishes.DEL_FLAG_NORMAL.equals(dishes.getDelFlag())) {// 菜品不存在
				notExists.add(dishesId);
				continue;
			}
			/**wjh注解，需要修改
			 * if (!UserUtils.hasPermission(dishes.getCompany())) {
				throw new BusinessException();// 非法操作，非本公司的菜品信息
			}*/
			float price = dishes.getPrice();
			orderDetail = new OrderDetail();
			orderDetail.setOrderId(orderId);
			orderDetail.setDishesId(dishesId);
			orderDetail.setDishesName(dishes.getName());
			orderDetail.setDishesPrice(price);
			orderDetail.setNum(num);
			orderDetail.setCreateBy(user);
			orderDetail.setCreateDate(new Date());
			orderDetailsList.add(orderDetail);
			money += dishes.getPrice();
		}
		if (!notExists.isEmpty()) {
			throw new BusinessException("有不存在的菜品", Response.NOTLOGIN, notExists);// 返回已经存在的菜品
		}
		if (orderDetailsList.isEmpty()) {
			throw new BusinessException("订单中没有任何菜肴");
		}
		orderDetailDao.save(orderDetailsList);
		order.setState(Config.STATE_MAKE_ORDER);// 已经下单
		order.setMoney(money);
		order.setUpdateBy(user);
		order.setUpdateDate(new Date());
		orderDao.save(order);
		return order;
	}

	/**
	 * 追单
	 * 
	 * @param orderId
	 * @param orderDetails
	 * @return
	 */
	@Transactional(readOnly = false)
	public Object addToOrder(String orderId, String[] orderDetails) {
		Order order = orderDao.get(orderId);
		if (order == null || !Order.DEL_FLAG_NORMAL.equals(order.getDelFlag())) {
			throw new BusinessException("订单不存在",
					Response.CODE_NEEDREFRESH_ORDER, order);
		}
		if (!StringUtils.isEmpty(order.getParentId())) {// 子订单不允许追单
			throw new BusinessException("子订单不允许追单");
		}
		User user = UserUtils.getUser();
		if (!UserUtils.hasPermission(order.getCompany())) {
			throw new BusinessException();
		}
		switch (order.getState()) {
		case Config.STATE_OPEN_TABLE:// 开台的状态允许下单
			throw new BusinessException("请直接下单",
					Response.CODE_NEEDREFRESH_ORDER, order);
		case Config.STATE_LEAVE_TABLE:
			throw new BusinessException("目前未在任何桌台上，不允许下单",
					Response.CODE_NEEDREFRESH_ORDER, order);
		case Config.STATE_CHARGED:
			throw new BusinessException("已经结账不允许下单",
					Response.CODE_NEEDREFRESH_ORDER, order);
		default:// 其他状态允许下单
			break;
		}

		if (orderDetails == null || orderDetails.length == 0) {
			throw new BusinessException("请选择需要下单的菜品");
		}
		List<OrderDetail> orderDetailsList = new LinkedList<OrderDetail>();
		OrderDetail orderDetail;
		Set<String> notExists = new HashSet<String>();

		float money = 0;
		Order addedOrder = new Order();
		addedOrder.setState(Config.STATE_MAKE_ORDER);// 已经下单
		// order.setMoney(money);
		addedOrder.setCreateBy(user);
		addedOrder.setUpdateDate(new Date());
		addedOrder.setParentId(orderId);
		orderDao.save(addedOrder);

		String newOrderId = addedOrder.getId();
		for (String orderInfo : orderDetails) {
			String[] dets = orderInfo.split(":");
			if (dets == null || dets.length == 0) {
				continue;
			}
			int num = 1;// 数量默认为1
			if (dets.length > 1) {
				try {
					num = Integer.parseInt(dets[1]);
				} catch (Exception e) {
					num = 0;// 传值错误则不下单
				}
			}
			if (num < 1) {// 数量小于1则不下单
				continue;
			}
			String dishesId = dets[0];
			Dishes dishes = dishesDao.get(dishesId);
			if (dishes == null
					|| !Dishes.DEL_FLAG_NORMAL.equals(dishes.getDelFlag())) {// 菜品不存在
				notExists.add(dishesId);
				continue;
			}/**wjh注解，需要修改
			if (!UserUtils.hasPermission(dishes.getCompany())) {
				throw new BusinessException();// 非法操作，非本公司的菜品信息
			}*/
			float price = dishes.getPrice();
			orderDetail = new OrderDetail();
			orderDetail.setOrderId(newOrderId);
			orderDetail.setDishesId(dishesId);
			orderDetail.setDishesName(dishes.getName());
			orderDetail.setDishesPrice(price);
			orderDetail.setNum(num);
			orderDetail.setCreateBy(user);
			orderDetail.setCreateDate(new Date());
			orderDetailsList.add(orderDetail);
			money += dishes.getPrice();
		}
		if (!notExists.isEmpty()) {
			throw new BusinessException("有不存在的菜品", Response.NOTLOGIN, notExists);// 返回已经存在的菜品
		}
		if (orderDetailsList.isEmpty()) {
			throw new BusinessException("订单中没有任何菜肴");
		}
		orderDetailDao.save(orderDetailsList);
		addedOrder.setMoney(money);
		orderDao.save(addedOrder);
		return order;
	}
}
