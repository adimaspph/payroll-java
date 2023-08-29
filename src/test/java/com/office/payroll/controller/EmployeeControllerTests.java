package com.office.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.payroll.exception.custom.EmployeeNotFoundException;
import com.office.payroll.model.Employee;
import com.office.payroll.model.Gender;
import com.office.payroll.model.Payroll;
import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.service.EmployeeService;
import com.office.payroll.service.SalaryMatrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private SalaryMatrixService salaryMatrixService;

    @Autowired
    EmployeeController employeeController;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenUserControllerInjected_thenNotNull() {
        assertThat(employeeController).isNotNull();
    }

    @Test
    public void whenGetAllEmployees_thenCorrectResponse() throws Exception {
        Employee employee1 = new Employee("1", "John Doe", Gender.MALE, 1, true, null);
        Employee employee2 = new Employee("2", "Jane Smith", Gender.FEMALE, 2, false, null);
        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Jane Smith"));
    }

    @Test
    public void testGetAllEmployeesPage() throws Exception {
        // Mock data
        List<Employee> employees = Arrays.asList(
                new Employee("1", "John Doe", Gender.MALE, 3, true, new ArrayList<>()),
                new Employee("2", "Jane Smith", Gender.FEMALE, 2, false, new ArrayList<>())
        );

        when(employeeService.getAllEmployeesPage(0, 10)).thenReturn(employees);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/page"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("John Doe"))
                .andExpect(jsonPath("$.data[0].gender").value("MALE"))
                .andExpect(jsonPath("$.data[0].grade").value(3))
                .andExpect(jsonPath("$.data[0].married").value(true))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.data[1].gender").value("FEMALE"))
                .andExpect(jsonPath("$.data[1].grade").value(2))
                .andExpect(jsonPath("$.data[1].married").value(false));

        verify(employeeService, times(1)).getAllEmployeesPage(0, 10);
    }

    @Test
    public void whenGetEmployeeById_thenCorrectResponse() throws Exception {
        Employee employee = new Employee("1", "John Doe", Gender.MALE, 1, true, null);

        when(employeeService.getEmployeeById("1")).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void whenGetEmployeeByIdInvalidId_thenErrorResponse() throws Exception {
        when(employeeService.getEmployeeById(anyString())).thenThrow(new EmployeeNotFoundException("Employee ID not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreateEmployee_thenCorrectResponse() throws Exception {
        Employee employee = new Employee(null, "John Doe", Gender.MALE, 1, true, null);
        Employee savedEmployee = new Employee("1", "John Doe", Gender.MALE, 1, true, null);

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void whenCreateEmployeeInvalidName_thenErrorResponse() throws Exception {
        Employee employee = new Employee(null, "", Gender.MALE, 1, true, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreateEmployeeInvalidGender_thenErrorResponse() throws Exception {

        String body = """
                {
                  "name": "John Doe",
                  "gender": "BISEX",
                  "grade": 0,
                  "married": true
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreateEmployeeNullGrade_thenErrorResponse() throws Exception {

        String body = """
                {
                  "name": "John Doe",
                  "gender": "bisex",
                  "married": true
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenGetAllPayrollEmployeeById_thenCorrectResponse() throws Exception {
        Employee employee = new Employee("1", "John Doe", Gender.MALE, 1, true, new ArrayList<>());
        SalaryMatrix salaryMatrix = new SalaryMatrix(1L, 1, 100.0, 200.0, 300.0, 400.0);
        Payroll payroll = new Payroll(1L, 100.0, 100.0, 100.0, 6, 2023, employee);
        Payroll payroll2 = new Payroll(2L, 200.0, 200.0, 200.0, 7, 2023, employee);
        employee.getPayrolls().add(payroll);
        employee.getPayrolls().add(payroll2);

        when(employeeService.getEmployeeById("1")).thenReturn(employee);
        when(salaryMatrixService.getSalaryMatrixByGrade(1)).thenReturn(salaryMatrix);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1/payroll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employee.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employee.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employee.gender").value("MALE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employee.grade").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.employee.married").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].total_paycut").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].total_allowance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].head_of_family").value(400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].basic_salary").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].total_salary").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].month").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[0].year").value(2023))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].total_paycut").value(200.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].total_allowance").value(200.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].head_of_family").value(400.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].basic_salary").value(200.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].total_salary").value(200.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].month").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.payroll_detail_responses[1].year").value(2023));
    }

    @Test
    public void whenGetAllPayrollEmployeeByIdInvalid_thenErrorResponse() throws Exception {

        when(employeeService.getEmployeeById(anyString())).thenThrow(new EmployeeNotFoundException("Id not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1/payroll"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenUpdateEmployee_thenCorrectResponse() throws Exception {
        Employee updatedEmployee = new Employee("1", "John Updated", Gender.MALE, 2, false, null);

        when(employeeService.updateEmployee(eq("1"), any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("John Updated"));
    }

    @Test
    public void whenUpdateEmployeeInvalidId_thenCorrectResponse() throws Exception {
        Employee updatedEmployee = new Employee("1", "John Updated", Gender.MALE, 2, false, null);

        when(employeeService.updateEmployee(eq("9"), any(Employee.class))).thenThrow(new EmployeeNotFoundException("id not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenDeleteEmployee_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(employeeService, times(1)).deleteEmployee("1");
    }

    @Test
    public void whenDeleteEmployeeInvalidId_thenErrorResponse() throws Exception {
        doThrow(new EmployeeNotFoundException("ID salary not found")).when(employeeService).deleteEmployee("9");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/9"))
                .andExpect(status().is4xxClientError());
    }
}

