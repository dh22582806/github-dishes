/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.eat.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.eat.entity.Dcategory;
import com.thinkgem.jeesite.modules.eat.entity.Floor;
import com.thinkgem.jeesite.modules.eat.entity.Room;
import com.thinkgem.jeesite.modules.eat.entity.Store;
import com.thinkgem.jeesite.modules.eat.service.DcategoryService;
import com.thinkgem.jeesite.modules.eat.service.FloorService;
import com.thinkgem.jeesite.modules.eat.service.RoomService;
import com.thinkgem.jeesite.modules.eat.service.StoreService;

/**
 * 门店Controller
 * 
 * @author wjh
 * @version 2014-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/eat/store")
public class StoreController extends BaseController {

	@Autowired
	private StoreService storeService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private FloorService floorService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private DcategoryService dcategoryService;

	@ModelAttribute
	public Store get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return storeService.get(id);
		} else {
			return new Store();
		}
	}

	@RequiresPermissions("eat:store:view")
	@RequestMapping(value = { "list", "" })
	public String list(Store store, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			store.setCreateBy(user);
		}
		Page<Store> page = storeService.find(
				new Page<Store>(request, response), store);
		model.addAttribute("page", page);
		return "modules/eat/storeList";
	}

	@RequiresPermissions("eat:store:view")
	@RequestMapping(value = "form")
	public String form(Store store, Model model) {
		model.addAttribute("store", store);
		return "modules/eat/storeForm";
	}

	@RequiresPermissions("eat:store:edit")
	@RequestMapping(value = "save")
	public String save(Store store, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, store)) {
			return form(store, model);
		}
		storeService.save(store);
		addMessage(redirectAttributes, "保存门店'" + store.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/eat/store/?repage";
	}

	@RequiresPermissions("eat:store:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		storeService.delete(id);
		addMessage(redirectAttributes, "删除门店成功");
		return "redirect:" + Global.getAdminPath() + "/eat/store/?repage";
	}

	/**
	 * 树结构选择框，在选择组织的基础上增加选择门店 节点可加入元素：第二标签名称pName，用于显示所在公司或所在部门、门店、楼层、房间、桌台
	 * 
	 * @param extId
	 * @param type
	 * @param grade
	 * @param level
	 *            显示树结构级别，1：公司级；2：门店级；3：楼层级；4：房间级；5：桌台级
	 * @param treeType
	 *            1:菜单类别；0：桌台类别
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeDataForEat")
	public List<Map<String, Object>> treeDataForEat(
			@RequestParam(required = false) Long extId,
			@RequestParam(required = false) Long type,
			@RequestParam(required = false) Long grade,
			@RequestParam(required = false) Long level,
			@RequestParam(required = false) Long treeType,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// User user = UserUtils.getUser();
		// 公司节点
		List<Office> list = officeService.findAll();

		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e
					.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && Integer.parseInt(e
							.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e
							.getGrade()) <= grade.intValue()))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() &&
				// e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId()
						: 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		// 加上门店节点
		if (level != null && level.intValue() >= 1) {
			Store store = new Store();
			Page<Store> storePage = storeService.find(new Page<Store>(), store);
			for (int i = 0; i < storePage.getList().size(); i++) {
				Store st = storePage.getList().get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", st.getId());
				map.put("pId", st.getCompany().getId() != null ? st
						.getCompany().getId() : 0);
				if (level.intValue() == 1) {
					map.put("pName", st.getCompany().getName() != null ? st
							.getCompany().getName() : "");// 第二标签名称pName，
				}
				map.put("name", st.getName());
				mapList.add(map);
			}
		}
		// 加上楼层节点
		if (level != null && level.intValue() >= 2) {
			if (treeType != null && treeType.intValue() == 1) {
				// 查找菜品类别
				Page<Dcategory> dcategoryPage = dcategoryService.find(
						new Page<Dcategory>(), new Dcategory());
				for (int i = 0; i < dcategoryPage.getList().size(); i++) {
					Dcategory dc = dcategoryPage.getList().get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", dc.getId());
					map.put("pId", dc.getStore().getId() != null ? dc.getStore()
							.getId() : 0);
					if (level.intValue() == 2) {
						StringBuffer sb = new StringBuffer();
						sb.append(dc.getStore().getCompany().getName())
								.append(";").append(dc.getStore().getName());
						map.put("pName", sb.toString());//
					}
					map.put("name", dc.getName());
					mapList.add(map);
				}
			} else {
				// 查找楼层节点
				Floor floor = new Floor();
				Page<Floor> floorPage = floorService.find(new Page<Floor>(),
						floor);
				for (int i = 0; i < floorPage.getList().size(); i++) {
					Floor f = floorPage.getList().get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", f.getId());
					map.put("pId", f.getStore().getId() != null ? f.getStore()
							.getId() : 0);
					if (level.intValue() == 2) {
						StringBuffer sb = new StringBuffer();
						sb.append(f.getStore().getCompany().getName())
								.append(";").append(f.getStore().getName());
						map.put("pName", sb.toString());//
					}
					map.put("name", f.getName());
					mapList.add(map);
				}
			}
			// 加上房间节点
			if (level != null && level.intValue() >= 3) {
				Room room = new Room();
				Page<Room> roomPage = roomService.find(new Page<Room>(), room);
				for (int i = 0; i < roomPage.getList().size(); i++) {
					Room r = roomPage.getList().get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", r.getId());
					map.put("pId", r.getFloor().getId() != null ? r.getFloor()
							.getId() : 0);
					if (level.intValue() == 3) {
						StringBuffer sb = new StringBuffer();
						sb.append(
								r.getFloor().getStore().getCompany().getName())
								.append(";")
								.append(r.getFloor().getStore().getName())
								.append(";").append(r.getFloor().getName());

						map.put("pName", sb.toString());//
					}
					map.put("name", r.getName());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}

}
