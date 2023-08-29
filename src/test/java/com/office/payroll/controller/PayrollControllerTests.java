package com.office.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.payroll.exception.custom.PayrollNotFoundException;
import com.office.payroll.model.Employee;
import com.office.payroll.model.Gender;
import com.office.payroll.model.Payroll;
import com.office.payroll.model.dto.request.PayrollRequestDTO;
import com.office.payroll.service.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PayrollController.class)
//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PayrollControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayrollService payrollService;

    @Autowired
    PayrollController payrollController;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenUserControllerInjected_thenNotNull() {
        assertThat(payrollController).isNotNull();
    }

    @Test
    public void whenGetPayrollById_thenCorrectResponse() throws Exception {
        Payroll payroll = new Payroll(1L, 1000.0, 100.0, 200.0, 6, 2023, null);

        when(payrollService.getPayrollById(1L)).thenReturn(payroll);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payrolls/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.basic_payroll").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.paycut").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.additional_salary").value(200.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.year").value(2023));
    }

    @Test
    public void whenGetPayrollByIdInvalidId_thenErrorResponse() throws Exception {
        when(payrollService.getPayrollById(anyLong())).thenThrow(new PayrollNotFoundException("Payroll not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payrolls/9"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCreatePayrollValid_thenCorrectResponse() throws Exception {
        Employee employee = new Employee("1", "Adimas", Gender.MALE, 1, false, null);
        PayrollRequestDTO payrollRequest = new PayrollRequestDTO("1", 20, 1, 6, 2023);
        Payroll createdPayroll = new Payroll(1L, 1200.0, 150.0, 250.0, 6, 2023, employee);

        when(payrollService.createPayrollFromInput("1", 20, 1, 6, 2023)).thenReturn(createdPayroll);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payrollRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.basic_payroll").value(1200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.paycut").value(150))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.additional_salary").value(250))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.month").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.year").value(2023));
    }

    @Test
    public void whenCreatePayrollInvalidEmployeeId_thenErrorResponse() throws Exception {
        String user = """
                {
                  "employeeId": 1,
                  "daysWorked": 20,
                  "daysAbsent": 1,
                  "month": 6,
                  "year": 2023
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreatePayrollInvalidDaysWorked_thenErrorResponse() throws Exception {
        String payroll = """
                {
                  "employeeId": "1",
                  "daysWorked": -1,
                  "daysAbsent": 1,
                  "month": 6,
                  "year": 2023
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payroll)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreatePayrollInvalidDaysAbsent_thenErrorResponse() throws Exception {
        String payroll = """
                {
                  "employeeId": "1",
                  "daysWorked": 20,
                  "daysAbsent": -1,
                  "month": 6,
                  "year": 2023
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payroll)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreatePayrollInvalidMonth_thenErrorResponse() throws Exception {
        String payroll = """
                {
                  "employeeId": "1",
                  "daysWorked": 20,
                  "daysAbsent": 1,
                  "month": -6,
                  "year": 2023
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payroll)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void whenCreatePayrollInvalidYear_thenErrorResponse() throws Exception {
        String payroll = """
                {
                  "employeeId": "1",
                  "daysWorked": 20,
                  "daysAbsent": 1,
                  "month": 6,
                  "year": -2023
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payroll)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}

