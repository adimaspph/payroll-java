package com.office.payroll.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseTemplate<T> {
    @LastModifiedDate
    LocalDateTime timestamp;
    String status;
    T data;

    public void statusOk(T data) {
        this.timestamp = LocalDateTime.now();
        this.status = HttpStatus.OK.toString();
        this.data = data;
    }
}
