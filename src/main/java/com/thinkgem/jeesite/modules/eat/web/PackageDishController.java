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
import com.thinkgem.jeesite.modules.eat.entity.PackageDish;
import com.thinkgem.jeesite.modules.eat.service.PackageDishService;

/**
 * 套餐明细Controller
 * @author wjh
 * @version 2014-11-12
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/packageDish")
public class PackageDishController extends BaseController {

	@Autowired
	private PackageDishService packageDishService;
	
	@ModelAttribute
	public PackageDish get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return packageDishService.get(id);
		}else{
			return new PackageDish();
		}
	}
	
	@RequiresPermissions("eat:packageDish:view")
	@RequestMapping(value = {"list", ""})
	public String list(PackageDish packageDish, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			packageDish.setCreateBy(user);
		}
        Page<PackageDish> page = packageDishService.find(new Page<PackageDish>(request, response), packageDish); 
        model.addAttribute("page", page);
		return "modules/eat/packageDishList";
	}

	@RequiresPermissions("eat:packageDish:view")
	@RequestMapping(value = "form")
	public String form(PackageDish packageDish, Model model) {
		model.addAttribute("packageDish", packageDish);
		return "modules/eat/packageDishForm";
	}

	@RequiresPermissions("eat:packageDish:edit")
	@RequestMapping(value = "save")
	public String save(PackageDish packageDish, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, packageDish)){
			return form(packageDish, model);
		}
		packageDishService.save(packageDish);
		addMessage(redirectAttributes, "保存套餐明细'" + packageDish.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/eat/packageDish/?repage";
	}
	
	@RequiresPermissions("eat:packageDish:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		packageDishService.delete(id);
		addMessage(redirectAttributes, "删除套餐明细成功");
		return "redirect:"+Global.getAdminPath()+"/modules/eat/packageDish/?repage";
	}

}
