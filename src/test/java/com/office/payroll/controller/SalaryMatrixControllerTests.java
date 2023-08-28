package com.office.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.payroll.model.SalaryMatrix;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryMatrixController.class)
//@RunWith(SpringRunner.class)
public class SalaryMatrixControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalaryMatrixService salaryMatrixService;

    @Autowired
    private SalaryMatrixController salaryMatrixController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenUserControllerInjected_thenNotNull() {
        assertThat(salaryMatrixController).isNotNull();
    }

    @Test
    public void whenGetAllSalaryMatrices_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix1 = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);
        SalaryMatrix matrix2 = new SalaryMatrix(2L, 2, 1200.0, 150.0, 250.0, 0.0);
        List<SalaryMatrix> matrixList = Arrays.asList(matrix1, matrix2);

        when(salaryMatrixService.getAllSalaryMatrices()).thenReturn(matrixList);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/salary-matrices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].grade").value(1))
                .andExpect(jsonPath("$.data[0].basic_salary").value(1000.0))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].grade").value(2))
                .andExpect(jsonPath("$.data[1].basic_salary").value(1200.0));

        verify(salaryMatrixService, times(1)).getAllSalaryMatrices();
    }


    @Test
    public void whenCreateSalaryMatrixValid_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);

        when(salaryMatrixService.createSalaryMatrix(any(SalaryMatrix.class))).thenReturn(matrix);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.grade").value(1))
                .andExpect(jsonPath("$.data.basic_salary").value(1000.0));

        verify(salaryMatrixService, times(1)).createSalaryMatrix(any(SalaryMatrix.class));
    }

    @Test
    public void whenCreateSalaryMatrixInvalidBasicSalary_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, -1, 100.0, 200.0, 0.0);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateSalaryMatrixInvalidPaycut_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000, -100.0, 200.0, 0.0);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateSalaryMatrixInvalidAllowance_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000, 100.0, -200.0, 0.0);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateSalaryMatrixInvalidHeadOfFamily_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000, 100.0, 200.0, -1);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenGetSalaryMatrixById_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);

        when(salaryMatrixService.getSalaryMatrixById(1L)).thenReturn(matrix);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/salary-matrices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.grade").value(1))
                .andExpect(jsonPath("$.data.basic_salary").value(1000.0));

        verify(salaryMatrixService, times(1)).getSalaryMatrixById(1L);
    }

    @Test
    public void whenDeleteSalaryMatrix_thenCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/salary-matrices/1"))
                .andExpect(status().isNoContent());

        verify(salaryMatrixService, times(1)).deleteSalaryMatrix(1L);
    }

    @Test
    public void whenUpdateSalaryMatrix_thenCorrectResponse() throws Exception {
        // Mock data
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);

        when(salaryMatrixService.updateSalaryMatrix(eq(1L), any(SalaryMatrix.class))).thenReturn(matrix);

        // Perform PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/salary-matrices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.grade").value(1))
                .andExpect(jsonPath("$.data.basic_salary").value(1000.0));

        verify(salaryMatrixService, times(1)).updateSalaryMatrix(eq(1L), any(SalaryMatrix.class));
    }
}


