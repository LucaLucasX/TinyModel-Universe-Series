package com.reggie.controller;

import com.reggie.common.R;
import com.reggie.entity.Employee;
import com.reggie.common.PageBean;
import com.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据页面提交的用户名查询数据库
       Employee emp =employeeService.getEmpByUsername(employee.getUsername());
        if(emp==null){
            return R.error("登录失败");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @PostMapping("logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> add(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工,员工信息：{}",employee.toString());
        //新增员工初始化密码123456，md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录用户的id
        Long empID=(Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empID);
        employee.setUpdateUser(empID);
        employeeService.add(employee);
        return R.success("新增员工成功");
    }
    @GetMapping("/page")
    public R<PageBean> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name= {}",page,pageSize,name);
        PageBean pageBean = employeeService.page(page,pageSize,name);
       // log.info("pageBena is {}",pageBean);
        return R.success(pageBean);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empID=(Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empID);
        if(employee.getUsername()==null){
            employeeService.updateStatusByID(employee);
            return R.success("员工状态修改成功！");
        }
      else {employeeService.updateByID(employee);
            return R.success("员工信息修改成功！");
      }
    }
    @GetMapping("/{id}")
    public R<Employee> getByID(@PathVariable Long id){
        log.info("根据id查询员工信息....");
        Employee employee=employeeService.getByID(id);
        return R.success(employee);
    }
}
