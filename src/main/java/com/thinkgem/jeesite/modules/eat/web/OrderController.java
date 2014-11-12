/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.eat.entity.Order;
import com.thinkgem.jeesite.modules.eat.service.OrderService;

/**
 * 订单Controller
 * @author ThinkGem
 * @version 2014-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/order")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;
	
	@ModelAttribute
	public Order get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return orderService.get(id);
		}else{
			return new Order();
		}
	}
	
	@RequiresPermissions("eat:order:view")
	@RequestMapping(value = {"list", ""})
	public String list(Order order, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			order.setCreateBy(user);
		}
        Page<Order> page = orderService.find(new Page<Order>(request, response), order); 
        model.addAttribute("page", page);
		return "modules/eat/orderList";
	}

	@RequiresPermissions("eat:order:view")
	@RequestMapping(value = "form")
	public String form(Order order, Model model) {
		model.addAttribute("order", order);
		return "modules/eat/orderForm";
	}

	@RequiresPermissions("eat:order:edit")
	@RequestMapping(value = "save")
	public String save(Order order, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, order)){
			return form(order, model);
		}
		orderService.save(order);
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/modules/eat/order/?repage";
	}
	
	@RequiresPermissions("eat:order:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		orderService.delete(id);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/modules/eat/order/?repage";
	}

}
