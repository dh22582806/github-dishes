package com.thinkgem.jeesite.modules.eat.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.modules.eat.entity.Dcategory;

@Service
public class InitService {
	@Autowired
	private DcategoryService categoryService;
	@Autowired
	private DishesService dishesService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private TableService tableService;

	public Object doInit(Date updateTime) {

		Map<String, Object> datas = new HashMap<String, Object>();

		Long countCate = null;
		if (updateTime != null) {
			countCate = categoryService.countDateChanged(updateTime);
		}
		if (updateTime == null || countCate > 0) {
			List<Dcategory> cates = categoryService.findAll();
			datas.put("cates", cates);
		}

		Long countDishes = null;
		if (updateTime != null) {
			countDishes = dishesService.countDateChanged(updateTime);
		}
		if (updateTime == null || countDishes > 0) {
			Object dishes = dishesService.findAll();
			datas.put("dishes", dishes);
		}

		Long countRooms = null;
		if (updateTime != null) {
			countRooms = roomService.countDateChanged(updateTime);
		}
		if (updateTime == null || countRooms > 0) {
			Object rooms = roomService.findAll();
			datas.put("rooms", rooms);
		}

		Long countTables = null;
		if (updateTime != null) {
			countTables = tableService.countDateChanged(updateTime);
		}
		if (updateTime == null || countTables > 0) {
			Object tables = tableService.findAll();
			datas.put("tables", tables);
		}

		if (!datas.isEmpty()) {
			datas.put("update", Calendar.getInstance().getTimeInMillis());
		}
		return datas;
	}
}
