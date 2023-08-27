package com.office.payroll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.service.SalaryMatrixService;
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
import java.util.List;
import static org.mockito.Mockito.*;

@WebMvcTest(SalaryMatrixController.class)
public class SalaryMatrixControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalaryMatrixService salaryMatrixService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSalaryMatrices() throws Exception {
        SalaryMatrix matrix1 = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);
        SalaryMatrix matrix2 = new SalaryMatrix(2L, 2, 1200.0, 150.0, 250.0, 0.0);
        List<SalaryMatrix> matrixList = Arrays.asList(matrix1, matrix2);

        when(salaryMatrixService.getAllSalaryMatrices()).thenReturn(matrixList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/salary-matrices"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].grade").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].basicSalary").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].grade").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].basicSalary").value(1200.0));
    }

    @Test
    public void testGetSalaryMatrixById() throws Exception {
        SalaryMatrix matrix = new SalaryMatrix(1L, 1, 1000.0, 100.0, 200.0, 0.0);

        when(salaryMatrixService.getSalaryMatrixById(1L)).thenReturn(matrix);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/salary-matrices/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicSalary").value(1000.0));
    }

    @Test
    public void testCreateSalaryMatrix() throws Exception {
        SalaryMatrix matrix = new SalaryMatrix(null, 2, 1200.0, 150.0, 250.0, 0.0);
        SalaryMatrix createdMatrix = new SalaryMatrix(1L, 2, 1200.0, 150.0, 250.0, 0.0);

        when(salaryMatrixService.createSalaryMatrix(any(SalaryMatrix.class))).thenReturn(createdMatrix);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/salary-matrices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matrix)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicSalary").value(1200.0));
    }

    @Test
    public void testUpdateSalaryMatrix() throws Exception {
        SalaryMatrix updatedMatrix = new SalaryMatrix(1L, 2, 1500.0, 100.0, 300.0, 0.0);

        when(salaryMatrixService.updateSalaryMatrix(eq(1L), any(SalaryMatrix.class))).thenReturn(updatedMatrix);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/salary-matrices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMatrix)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.basicSalary").value(1500.0));
    }

    @Test
    public void testDeleteSalaryMatrix() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/salary-matrices/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(salaryMatrixService, times(1)).deleteSalaryMatrix(1L);
    }

    // Add more tests for other scenarios and edge cases

}

