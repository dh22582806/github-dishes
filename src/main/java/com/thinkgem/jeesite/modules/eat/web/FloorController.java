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
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.eat.entity.Floor;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.service.FloorService;
import com.thinkgem.jeesite.modules.eat.service.StoreService;

/**
 * 楼层Controller
 * @author wjh
 * @version 2014-11-04
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/floor")
public class FloorController extends BaseController {

	@Autowired
	private FloorService floorService;
	@Autowired
	StoreService storeService;
	
	@ModelAttribute
	public Floor get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return floorService.get(id);
		}else{
			return new Floor();
		}
	}
	
	@RequiresPermissions("eat:floor:view")
	@RequestMapping(value = {"list", ""})
	public String list(Floor floor, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			floor.setCreateBy(user);
		}
        Page<Floor> page = floorService.find(new Page<Floor>(request, response), floor); 
        model.addAttribute("page", page);
		return "modules/eat/floorList";
	}

	@RequiresPermissions("eat:floor:view")
	@RequestMapping(value = "form")
	public String form(Floor floor, Model model) {
		//新增楼层时默认填写一个归属门店
		if(StringUtils.isBlank(floor.getId())){
			 Page<Store> storePage = storeService.find(new Page<Store>(), new Store());
			if(storePage.getList()!=null&&!storePage.getList().isEmpty()){
				floor.setStore(storePage.getList().get(0));
			}
		}
		model.addAttribute("floor", floor);
		return "modules/eat/floorForm";
	}

	@RequiresPermissions("eat:floor:edit")
	@RequestMapping(value = "save")
	public String save(Floor floor, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, floor)){
			return form(floor, model);
		}
		floorService.save(floor);
		addMessage(redirectAttributes, "保存楼层'" + floor.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/eat/floor/?repage";
	}
	
	@RequiresPermissions("eat:floor:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		floorService.delete(id);
		addMessage(redirectAttributes, "删除楼层成功");
		return "redirect:"+Global.getAdminPath()+"/eat/floor/?repage";
	}

}
