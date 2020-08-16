package com.atguigu.crud.service;

import com.atguigu.crud.bena.Department;
import com.atguigu.crud.dao.DepartmentMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	public List<Department> getDepts() {
		// TODO Auto-generated method stub
		List<Department> departments = departmentMapper.selectByExample(null);
		return departments;
	}

}
