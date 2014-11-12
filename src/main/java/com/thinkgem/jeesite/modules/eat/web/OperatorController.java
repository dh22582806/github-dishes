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
import com.thinkgem.jeesite.modules.eat.entity.Operator;
import com.thinkgem.jeesite.modules.eat.service.OperatorService;

/**
 * 操作员Controller
 * @author ThinkGem
 * @version 2014-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/operator")
public class OperatorController extends BaseController {

	@Autowired
	private OperatorService operatorService;
	
	@ModelAttribute
	public Operator get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return operatorService.get(id);
		}else{
			return new Operator();
		}
	}
	
	@RequiresPermissions("eat:operator:view")
	@RequestMapping(value = {"list", ""})
	public String list(Operator operator, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			operator.setCreateBy(user);
		}
        Page<Operator> page = operatorService.find(new Page<Operator>(request, response), operator); 
        model.addAttribute("page", page);
		return "modules/eat/operatorList";
	}

	@RequiresPermissions("eat:operator:view")
	@RequestMapping(value = "form")
	public String form(Operator operator, Model model) {
		model.addAttribute("operator", operator);
		return "modules/eat/operatorForm";
	}

	@RequiresPermissions("eat:operator:edit")
	@RequestMapping(value = "save")
	public String save(Operator operator, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, operator)){
			return form(operator, model);
		}
		operatorService.save(operator);
		addMessage(redirectAttributes, "保存操作员'" + operator.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/eat/operator/?repage";
	}
	
	@RequiresPermissions("eat:operator:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		operatorService.delete(id);
		addMessage(redirectAttributes, "删除操作员成功");
		return "redirect:"+Global.getAdminPath()+"/eat/operator/?repage";
	}

}
