package com.atguigu.crud.bena;

import javax.validation.constraints.Pattern;

public class Employee {
    private Integer empId;
    
    @Pattern(regexp="(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})" 
    		,  message="用户名必须是6-16位数字和字母的组合或者2-5位中文")
    private String empName;

    private String gender;

    public Employee() {
		super();
	}

	public Employee(Integer empId, String empName, String gender, String emaill, Integer dId) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.gender = gender;
		this.emaill = emaill;
		this.dId = dId;
	}
	
	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", gender=" + gender + ", emaill=" + emaill
				+ ", dId=" + dId + ", department=" + department + "]";
	}

	//@Email
    @Pattern(regexp="^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$" 
    		,  message="邮箱格式不正确")
	private String emaill;

    private Integer dId;
    
    //希望查询员工的同时部门信息也是查询好的。
    private Department department;

    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getEmaill() {
        return emaill;
    }

    public void setEmaill(String emaill) {
        this.emaill = emaill == null ? null : emaill.trim();
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }
}