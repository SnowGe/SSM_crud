package com.atguigu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bena.Employee;
import com.atguigu.crud.bena.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/*
 * 处理员工CRUD请求
 * */

@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	/**
	 * 导入jackson包，负责将对象转换为json对象
	 * @param pn
	 * @return
	 */	
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn)
	{
		//service层的组件帮我们查出员工的所有数据
		//引入PageHelper分页插件
		//在查询之前，只需要调用,传入页码，以及每页的大小。
		PageHelper.startPage(pn, 5);
		//startPage后面紧跟的查询就是一个分页查询。
		List<Employee> emps = employeeService.getAll();
		//使用pageinfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		//封装了详细的分页信息，包括我们查出来的数据.传入连续显示的页数
		PageInfo page = new PageInfo(emps,5);
		
		//return page;
		return Msg.succes().Add("pageInfo",page);
		/**
		 * 和return Msg.succes().Add("pageInfo",page);
		 * 等价：
		 *  Map<String, Object> map = new HashMap<String, Object>();
			map.put("pageInfo", page);
			Msg msg = new Msg(100,"处理成功", map);
			return msg;
		 */
		
	}
	
	
	
	/*
	 * 查询员工数据（分页查询）
	 */
	//@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			Model model)
	{		
		//service层的组件帮我们查出员工的所有数据
		//引入PageHelper分页插件
		//在查询之前，只需要调用,传入页码，以及没页的大小。
		PageHelper.startPage(pn, 5);
		//startPage后面紧跟的查询就是一个分页查询。
		List<Employee> emps = employeeService.getAll();
		//使用pageinfo包装查询后的结果，只需要将pageInfo交给页面就行了。
		//封装了详细的分页信息，包括我们查出来的数据.传入连续显示的页数
		PageInfo page = new PageInfo(emps,5);
		
		model.addAttribute("pageInfo",page);
		
		return "list";
	}
	
	/**
	 * 员工保存
	 * 1.支持JSR303校验
	 * 2.导入Hibernate-Validator
	 * @return
	 */
	
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result){
		
		if(result.hasErrors()) {
			//校验失败，应该返回失败，在模态框中显示校验失败的错误信息
			Map<String,Object> map= new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors(); //提取出错误信息
			for(FieldError fieldError : errors){
				System.out.println("错误的字段名:"+ fieldError.getField());  
				System.out.println("错误信息:"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			
			return Msg.fail().Add("errorFields", map);
		}else {
			employeeService.saveEmp(employee);
			
			return Msg.succes();
		}
		
	}
	
	/**
	 * 检查用户名是否可用
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkUser")
	public Msg checkUser(@RequestParam("empName")String empName) {
		//先判断用户名是否是合法的表达式。
		String regx ="(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
		if(!empName.matches(regx)) {
			return Msg.fail().Add("va_msg", "用户名必须是6-16位数字和字母的组合或者2-5位中文");
		}
		
		//数据库用户名重复校验
		boolean b =  employeeService.checkUser(empName);
		if(b) {
			return Msg.succes();
		}
		else {
			return Msg.fail().Add("va_msg", "用户名不可用");
		}
		
	}
	
	/**
	 * 更具ID查询员工
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		
		Employee employee = employeeService.getEmp(id);
		return Msg.succes().Add("emp", employee);
	}
	
	/**
	 * 如果直接发送ajax=put形式的请求，
	 * 封装的数据
	 * employee
	 * [empId=1014, empName=null, gender=null, email=null, dID=null]
	 * 
	 * 问题：
	 * 请求体中有数据；但是Employee对象封装不上，所以sql语句是 update tbl_emp  where emp_id = 1014
	 * 
	 * 原因：
	 * Tomcat:
	 * 		1.将请求体中的数据，封装成一个map.
	 * 		2. request.getParameter("empName"),就会从这个map中取值
	 * 		3.springMVC封装POJO对象的时候。
	 * 				会把POJO中每个属性的值，调用request.getParameter("email")；
	 * 
	 * AJAX发送PUT请求不能直接发
	 * 		PUT请求，请求体中的数据，request.getParameter("empName")拿不到
	 * 		原因：Tomacat一看是PUT请求，不会封装请求体中的数据为map，只有POST形式的请求才会封装请求体为map
	 * 
	 * org.apache.cataline.connector.Request--parseParameters()(3111行)
	 * 
	 * 解决方案：
	 * 要能直接支持发送PUT之类的请求还要封装请求体中的数据
	 * 配置上HttpPutFormContentFilter，作用：将请求体总的数据解析包装成一个map,request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
	 * 
	 * 更新员工
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee, HttpServletRequest request) {
		
		System.out.println("请求体中的值" +request.getParameter("gender"));
		System.out.println("将要更新的员工数据：" + employee);
		employeeService.updateEmp(employee);
		return Msg.succes();
	}
	
	/**
	 * 单个批量二合一
	 * 批量删除：1-2-3
	 * 单个删除：1
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("id")String ids) {
		//批量删除
		if(ids.contains("-")) {
			String[] str_ids = ids.split("-");
			//组装id的集合
			List<Integer> del_ids = new ArrayList<>();
			for(String id : str_ids) {
				del_ids.add(Integer.parseInt(id));
			}			
			employeeService.deleteBatch(del_ids);
			
		}else {
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		
		return Msg.succes();
	}
	
	
}
