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
import com.thinkgem.jeesite.modules.eat.entity.Floor;
import com.thinkgem.jeesite.modules.eat.entity.Room;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.service.FloorService;
import com.thinkgem.jeesite.modules.eat.service.RoomService;

/**
 * 房间Controller
 * 
 * @author ThinkGem
 * @version 2014-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/room")
public class RoomController extends BaseController {

	@Autowired
	private RoomService roomService;
	@Autowired
	private FloorService floorService;

	@ModelAttribute
	public Room get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return roomService.get(id);
		} else {
			return new Room();
		}
	}

	@RequiresPermissions("eat:room:view")
	@RequestMapping(value = { "list", "" })
	public String list(Room room, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			room.setCreateBy(user);
		}
		Page<Room> page = roomService.find(new Page<Room>(request, response),
				room);
		model.addAttribute("page", page);
		return "modules/eat/roomList";
	}

	@RequiresPermissions("eat:room:view")
	@RequestMapping(value = "form")
	public String form(Room room, Model model) {
		// 新增房间时默认填写一个归属楼层
		if (StringUtils.isBlank(room.getId())) {
			Page<Floor> floorPage = floorService.find(new Page<Floor>(),
					new Floor());
			if (floorPage.getList() != null && !floorPage.getList().isEmpty()) {
				room.setFloor(floorPage.getList().get(0));
			}
		}
		model.addAttribute("room", room);
		return "modules/eat/roomForm";
	}

	@RequiresPermissions("eat:room:edit")
	@RequestMapping(value = "save")
	public String save(Room room, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, room)) {
			return form(room, model);
		}
		roomService.save(room);
		addMessage(redirectAttributes, "保存房间'" + room.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/eat/room/?repage";
	}

	@RequiresPermissions("eat:room:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		roomService.delete(id);
		addMessage(redirectAttributes, "删除房间成功");
		return "redirect:" + Global.getAdminPath() + "/eat/room/?repage";
	}

}
