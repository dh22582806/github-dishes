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
import com.thinkgem.jeesite.modules.eat.entity.Packages;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.service.PackagesService;
import com.thinkgem.jeesite.modules.eat.service.StoreService;

/**
 * 套餐Controller
 * 
 * @author wjh
 * @version 2014-11-12
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/packages")
public class PackagesController extends BaseController {

	@Autowired
	private PackagesService packagesService;
	@Autowired
	StoreService storeService;

	@ModelAttribute
	public Packages get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return packagesService.get(id);
		} else {
			return new Packages();
		}
	}

	@RequiresPermissions("eat:packages:view")
	@RequestMapping(value = { "list", "" })
	public String list(Packages packages, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			packages.setCreateBy(user);
		}
		Page<Packages> page = packagesService.find(new Page<Packages>(request,
				response), packages);
		model.addAttribute("page", page);
		return "modules/eat/packagesList";
	}

	@RequiresPermissions("eat:packages:view")
	@RequestMapping(value = "form")
	public String form(Packages packages, Model model) {
		// 新增套餐信息时默认填写一个归属门店
		if (StringUtils.isBlank(packages.getId())) {
			Page<Store> storePage = storeService.find(new Page<Store>(),
					new Store());
			if (storePage.getList() != null && !storePage.getList().isEmpty()) {
				packages.setStore(storePage.getList().get(0));
			}
		}
		model.addAttribute("packages", packages);
		return "modules/eat/packagesForm";
	}

	@RequiresPermissions("eat:packages:edit")
	@RequestMapping(value = "save")
	public String save(Packages packages, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, packages)) {
			return form(packages, model);
		}
		packagesService.save(packages);
		addMessage(redirectAttributes, "保存套餐'" + packages.getName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/eat/packages/?repage";
	}

	@RequiresPermissions("eat:packages:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		packagesService.delete(id);
		addMessage(redirectAttributes, "删除套餐成功");
		return "redirect:" + Global.getAdminPath()
				+ "/eat/packages/?repage";
	}

}
