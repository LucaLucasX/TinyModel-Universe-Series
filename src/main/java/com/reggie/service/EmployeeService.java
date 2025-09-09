package com.reggie.service;

import com.reggie.entity.Employee;
import com.reggie.common.PageBean;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService{
      Employee getEmpByUsername(String username);

    void add(Employee employee);

    PageBean page(int page, int pageSize, String name);

    void updateByID(Employee employee);

    Employee getByID(Long id);

    void updateStatusByID(Employee employee);
}
