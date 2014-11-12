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
import com.thinkgem.jeesite.modules.eat.entity.Floor;
import com.thinkgem.jeesite.modules.eat.entity.Room;
import com.thinkgem.jeesite.modules.eat.entity.Table;
import com.thinkgem.jeesite.modules.eat.service.RoomService;
import com.thinkgem.jeesite.modules.eat.service.TableService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 桌台Controller
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/table")
public class TableController extends BaseController {

	@Autowired
	private TableService tableService;
	@Autowired
	private RoomService roomService;

	@ModelAttribute
	public Table get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return tableService.get(id);
		} else {
			return new Table();
		}
	}

	@RequiresPermissions("eat:table:view")
	@RequestMapping(value = { "list", "" })
	public String list(Table table, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			table.setCreateBy(user);
		}
		Page<Table> page = tableService.find(
				new Page<Table>(request, response), table);
		model.addAttribute("page", page);
		return "modules/eat/tableList";
	}

	@RequiresPermissions("eat:table:view")
	@RequestMapping(value = "form")
	public String form(Table table, Model model) {
		// 新增房间时默认填写一个归属楼层
		if (StringUtils.isBlank(table.getId())) {
			Page<Room> roomPage = roomService
					.find(new Page<Room>(), new Room());
			if (roomPage.getList() != null && !roomPage.getList().isEmpty()) {
				table.setRoom(roomPage.getList().get(0));
			}
		}
		model.addAttribute("table", table);
		return "modules/eat/tableForm";
	}

	@RequiresPermissions("eat:table:edit")
	@RequestMapping(value = "save")
	public String save(Table table, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, table)) {
			return form(table, model);
		}
		tableService.save(table);
		addMessage(redirectAttributes, "保存桌台'" + table.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/eat/table/?repage";
	}

	@RequiresPermissions("eat:table:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		tableService.delete(id);
		addMessage(redirectAttributes, "删除桌台成功");
		return "redirect:" + Global.getAdminPath() + "/eat/table/?repage";
	}

}
