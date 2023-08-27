//package com.office.payroll.service;
//
//import com.office.payroll.model.Employee;
//import com.office.payroll.model.Gender;
//import com.office.payroll.repository.EmployeeRepository;
//import com.office.payroll.service.impl.EmployeeServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class EmployeeServiceTests {
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    @InjectMocks
//    private EmployeeServiceImpl employeeService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetAllEmployees() {
//        Employee employee1 = new Employee("1", "John Doe", Gender.MALE, 1, true, null);
//        Employee employee2 = new Employee("2", "Jane Smith", Gender.FEMALE, 2, false, null);
//        List<Employee> employeeList = Arrays.asList(employee1, employee2);
//
//        when(employeeRepository.findAll()).thenReturn(employeeList);
//
//        List<Employee> result = employeeService.getAllEmployees();
//        assertThat(result).isEqualTo(employeeList);
//    }
//
//    @Test
//    public void testGetEmployeeById() {
//        Employee employee = new Employee("1", "John Doe", Gender.MALE, 1, true, null);
//
//        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
//
//        Employee result = employeeService.getEmployeeById("1");
//        assertThat(result).isEqualTo(employee);
//    }
//
//    @Test
//    public void testCreateEmployee() {
//        Employee employee = new Employee("1", "John Doe", Gender.MALE, 1, true, null);
//
//        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
//
//        Employee result = employeeService.createEmployee(employee);
//        assertThat(result).isEqualTo(employee);
//    }
//
//    @Test
//    public void testUpdateEmployee() {
//        Employee existingEmployee = new Employee("1", "John Doe", Gender.MALE, 1, true, null);
//        Employee updatedEmployee = new Employee("1", "John Updated", Gender.MALE, 2, false, null);
//
//        when(employeeRepository.existsById("1")).thenReturn(true);
//        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
//
//        Employee result = employeeService.updateEmployee("1", updatedEmployee);
//        assertThat(result).isEqualTo(updatedEmployee);
//    }
//
//    @Test
//    public void testDeleteEmployee() {
//        when(employeeRepository.existsById("1")).thenReturn(true);
//
//        employeeService.deleteEmployee("1");
//        verify(employeeRepository, times(1)).deleteById("1");
//    }
//
//    // Add more test cases for exception scenarios and other methods if needed
//}
//
