package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.PageBean;
import com.reggie.entity.Employee;
import com.reggie.mapper.EmployMapper;
import com.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceimpl  implements EmployeeService{
    @Autowired
    private EmployMapper employMapper;
    @Override
    public Employee getEmpByUsername(String username) {
              return employMapper.getEmpByusername(username);
    }

    @Override
    public void add(Employee employee) {
        employMapper.add(employee);
    }

    @Override
    public PageBean page(int page, int pageSize, String name) {
        //1、获取总记录数
        Long count = employMapper.count();

        //2、获取分页查询结果列表
        Integer S = (page - 1) * pageSize; //计算起始索引 , 公式: (页码-1)*页大小
        List<Object> empList =employMapper.list(S, pageSize, name);

        //3、封装PageBean对象
        return new PageBean(empList,count);
    }

    @Override
    public void updateByID(Employee employee) {
        employMapper.updateByID(employee);
    }

    @Override
    public Employee getByID(Long id) {
        return employMapper.getEmpByID(id);
    }

    @Override
    public void updateStatusByID(Employee employee) {
        employMapper.updateStatusByID(employee);
    }
}
