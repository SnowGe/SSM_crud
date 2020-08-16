package com.atguigu.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bena.Department;
import com.atguigu.crud.bena.Msg;
import com.atguigu.crud.service.DepartmentService;

/**
 * 处理和部门有关的请求
 * @author Snow
 *
 */

@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * 返回所有部门的信息
	 */
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts() {
		//查出的所有部门信息
		List<Department> departments = departmentService.getDepts();
		
		return Msg.succes().Add("depts", departments);
	}
}
