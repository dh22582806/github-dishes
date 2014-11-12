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
import com.thinkgem.jeesite.modules.eat.entity.Dcategory;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.service.DcategoryService;
import com.thinkgem.jeesite.modules.eat.service.StoreService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 类别Controller
 * 
 * @author ThinkGem
 * @version 2014-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/dcategory")
public class DcategoryController extends BaseController {

	@Autowired
	private DcategoryService dcategoryService;
	@Autowired
	StoreService storeService;

	@ModelAttribute
	public Dcategory get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dcategoryService.get(id);
		} else {
			return new Dcategory();
		}
	}

	@RequiresPermissions("eat:dcategory:view")
	@RequestMapping(value = { "list", "" })
	public String list(Dcategory dcategory, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			dcategory.setCreateBy(user);
		}
		Page<Dcategory> page = dcategoryService.find(new Page<Dcategory>(
				request, response), dcategory);
		model.addAttribute("page", page);
		return "modules/eat/dcategoryList";
	}

	@RequiresPermissions("eat:dcategory:view")
	@RequestMapping(value = "form")
	public String form(Dcategory dcategory, Model model) {
		// 新增菜品大类时默认填写一个归属门店
		if (StringUtils.isBlank(dcategory.getId())) {
			Page<Store> storePage = storeService.find(new Page<Store>(),
					new Store());
			if (storePage.getList() != null && !storePage.getList().isEmpty()) {
				dcategory.setStore(storePage.getList().get(0));
			}
		}
		model.addAttribute("dcategory", dcategory);
		return "modules/eat/dcategoryForm";
	}

	@RequiresPermissions("eat:dcategory:edit")
	@RequestMapping(value = "save")
	public String save(Dcategory dcategory, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dcategory)) {
			return form(dcategory, model);
		}
		dcategoryService.save(dcategory);
		addMessage(redirectAttributes, "保存类别'" + dcategory.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/eat/dcategory/?repage";
	}

	@RequiresPermissions("eat:dcategory:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		dcategoryService.delete(id);
		addMessage(redirectAttributes, "删除类别成功");
		return "redirect:" + Global.getAdminPath() + "/eat/dcategory/?repage";
	}

}
