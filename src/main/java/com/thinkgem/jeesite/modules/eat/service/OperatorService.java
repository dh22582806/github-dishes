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
import com.thinkgem.jeesite.modules.eat.entity.Operator;
import com.thinkgem.jeesite.modules.eat.dao.OperatorDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 操作员Service
 * @author ThinkGem
 * @version 2014-05-14
 */
@Component
@Transactional(readOnly = true)
public class OperatorService extends BaseService {

	@Autowired
	private OperatorDao operatorDao;
	
	public Operator get(String id) {
		return operatorDao.get(id);
	}
	
	public Page<Operator> find(Page<Operator> page, Operator operator) {
		DetachedCriteria dc = operatorDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(operator.getName())){
			dc.add(Restrictions.like("name", "%"+operator.getName()+"%"));
		}
		 dc.createAlias("company", "company");
			dc.add(dataScopeFilter(UserUtils.getUser(), "company", "createBy"));
		dc.add(Restrictions.eq(Operator.FIELD_DEL_FLAG, Operator.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return operatorDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Operator operator) {
		User user = UserUtils.getUser();
		operator.setCompany(user.getCompany());
		operatorDao.save(operator);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		operatorDao.deleteById(id);
	}
	
}
