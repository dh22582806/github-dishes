/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.web;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.modules.eat.bean.Response;
import com.thinkgem.jeesite.modules.eat.service.DcategoryService;
import com.thinkgem.jeesite.modules.eat.service.DishesService;
import com.thinkgem.jeesite.modules.eat.service.InitService;
import com.thinkgem.jeesite.modules.eat.service.OrderService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 菜品Controller
 * 
 * @author ThinkGem
 * @version 2014-05-02
 */
@Controller
@RequestMapping(value = "/api")
public class ApiController {
	private Logger log = Logger.getLogger(ApiController.class);

	@Autowired
	private DishesService dishesService;
	@Autowired
	private DcategoryService dDcategoryService;
	@Autowired
	private InitService initService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/doc")
	public String doc() {
		return "modules/eat/doc";
	}

	@ResponseBody
	@RequestMapping(value = "/getDishes")
	public Response getDishes(String categoryId) {
		if (StringUtils.isEmpty(categoryId)) {
			return Response.failure("类别不能为空");
		}
		return Response.success(dishesService
				.findByCategoryId(categoryId, true));
	}

	@ResponseBody
	@RequestMapping(value = "/getCategory")
	public Response getCategory() {
		return Response.success(dDcategoryService.findAll());
	}

	/**
	 * 登录
	 * 
	 * @param categoryCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doLogin")
	public Response doLogin(String account, String password) {
		User user = UserUtils.getUser();
		if (user.getId() != null) {
			return Response.success();
		}
		return Response.failure("登录失败");
	}

	/**
	 * 初始化数据
	 * 
	 * @param update
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doInit")
	public Response doInit(Long update) {
		try {
			Date updateTime = null;
			if (update != null) {
				updateTime = new Date(update);
			}
			return Response.success(initService.doInit(updateTime));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	/**
	 * 开台
	 * 
	 * @param tableId
	 * @return
	 */
	/**@ResponseBody
	@RequestMapping(value = "/openTable")
	public Response openTable(String tableId) {
		try {
			return Response.success(orderService.openTabble(tableId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}*/

	@ResponseBody
	@RequestMapping(value = "/getOrders")
	public Response getOrders() {
		try {
			return Response.success(orderService.getOrders());
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getOrderInfo")
	public Response getOrderInfo(String orderId) {
		try {
			return Response.success(orderService.getOrderInfoById(orderId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getOrderInfoByTableId")
	public Response getOrderInfoByTableId(String tableId) {
		try {
			return Response
					.success(orderService.getOrderInfoByTableId(tableId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/leaveTable")
	public Response leaveTable(String orderId) {
		try {
			return Response.success(orderService.leaveTable(orderId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	/**@ResponseBody
	@RequestMapping(value = "/changeTable")
	public Response changeTable(String orderId, String tableId) {
		try {
			return Response.success(orderService.changeTable(orderId, tableId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}*/

	@ResponseBody
	@RequestMapping(value = "/openOrder")
	public Response openOrder(String orderId, String[] orderDetails) {
		try {
			return Response.success(orderService.openOrder(orderId,
					orderDetails));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/Order")
	public Response addToOrder(String orderId, String[] orderDetails) {
		try {
			return Response.success(orderService.addToOrder(orderId,
					orderDetails));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/chargeOrder")
	public Response chargeOrder(String orderId) {
		try {
			return Response.success(orderService.chargeOrder(orderId));
		} catch (Throwable throwable) {
			log.error(throwable.getMessage(), throwable);
			return Response.failure(throwable);
		}
	}

}
