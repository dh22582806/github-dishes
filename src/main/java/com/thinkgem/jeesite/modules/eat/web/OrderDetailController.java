/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 订单Controller
 * 
 * @author ThinkGem
 * @version 2014-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/orderDetail")
public class OrderDetailController extends BaseController {

}
