package com.office.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.payroll.model.Payroll;
import com.office.payroll.service.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;

@WebMvcTest(PayrollController.class)
public class PayrollControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayrollService payrollService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPayrolls() throws Exception {
        Payroll payroll1 = new Payroll(1L, 1000.0, 100.0, 200.0, new Date(), null);
        Payroll payroll2 = new Payroll(2L, 1200.0, 150.0, 250.0, new Date(), null);
        List<Payroll> payrollList = Arrays.asList(payroll1, payroll2);

        when(payrollService.getAllPayrolls()).thenReturn(payrollList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payrolls"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].basicPayroll").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].basicPayroll").value(1200.0));
    }

    @Test
    public void testGetPayrollById() throws Exception {
        Payroll payroll = new Payroll(1L, 1000.0, 100.0, 200.0, new Date(), null);

        when(payrollService.getPayrollById(1L)).thenReturn(payroll);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/payrolls/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicPayroll").value(1000.0));
    }

    @Test
    public void testCreatePayroll() throws Exception {
        Payroll payroll = new Payroll(null, 1200.0, 150.0, 250.0, new Date(), null);
        Payroll createdPayroll = new Payroll(1L, 1200.0, 150.0, 250.0, new Date(), null);

        when(payrollService.createPayroll(any(Payroll.class))).thenReturn(createdPayroll);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payrolls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payroll)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicPayroll").value(1200.0));
    }

    @Test
    public void testUpdatePayroll() throws Exception {
        Payroll updatedPayroll = new Payroll(1L, 1500.0, 100.0, 300.0, new Date(), null);

        when(payrollService.updatePayroll(eq(1L), any(Payroll.class))).thenReturn(updatedPayroll);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/payrolls/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayroll)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicPayroll").value(1500.0));
    }

    @Test
    public void testDeletePayroll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/payrolls/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(payrollService, times(1)).deletePayroll(1L);
    }

    // Add more tests for other scenarios and edge cases

}

