/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.web;

import java.util.List;

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
import com.thinkgem.jeesite.modules.eat.entity.Dcategory;
import com.thinkgem.jeesite.modules.eat.entity.Dishes;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.exception.BusinessException;
import com.thinkgem.jeesite.modules.eat.service.DcategoryService;
import com.thinkgem.jeesite.modules.eat.service.DishesService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 菜品Controller
 * 
 * @author ThinkGem
 * @version 2014-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/dishes")
public class DishesController extends BaseController {

	@Autowired
	private DishesService dishesService;
	@Autowired
	private DcategoryService dcategoryService;

	@ModelAttribute
	public Dishes get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dishesService.get(id);
		} else {
			return new Dishes();
		}
	}

	@RequiresPermissions("eat:dishes:view")
	@RequestMapping(value = { "list", "" })
	public String list(Dishes dishes, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			dishes.setCreateBy(user);
		}
		Page<Dishes> page = dishesService.find(new Page<Dishes>(request,
				response), dishes, true);
		model.addAttribute("page", page);
		return "modules/eat/dishesList";
	}

	@RequiresPermissions("eat:dishes:view")
	@RequestMapping(value = "form")
	public String form(Dishes dishes, Model model) {
		// 新增菜品时默认填写一个归属菜品类别
		if (StringUtils.isBlank(dishes.getId())) {
			Page<Dcategory> dcategoryPage = dcategoryService.find(
					new Page<Dcategory>(), new Dcategory());
			if (dcategoryPage.getList() != null
					&& !dcategoryPage.getList().isEmpty()) {
				dishes.setDcategory(dcategoryPage.getList().get(0));
			}
		}
		model.addAttribute("dishes", dishes);
		return "modules/eat/dishesForm";
	}

	@RequiresPermissions("eat:dishes:edit")
	@RequestMapping(value = "save")
	public String save(Dishes dishes, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dishes)) {
			return form(dishes, model);
		}
		dishesService.save(dishes);
		addMessage(redirectAttributes, "保存菜品'" + dishes.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/eat/dishes/?repage";
	}

	@RequiresPermissions("eat:dishes:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		dishesService.delete(id);
		addMessage(redirectAttributes, "删除菜品成功");
		return "redirect:" + Global.getAdminPath() + "/eat/dishes/?repage";
	}

}
