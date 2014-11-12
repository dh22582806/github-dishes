/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.modules.eat.entity.Order;

/**
 * 订单DAO接口
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Repository
public class OrderDao extends BaseDao<Order> {
	public Object getResult() {
		return super
				.findBySql(
						"SELECT a.id AS tableId,a.code AS tableCode, b.id AS orderId, b.state FROM eat_table a,(SELECT id,table_id,state FROM eat_order WHERE state NOT IN(3,6) AND del_flag = 0) b WHERE a.id = b.table_id AND a.del_flag = 0",
						null, Map.class);
	}

}
