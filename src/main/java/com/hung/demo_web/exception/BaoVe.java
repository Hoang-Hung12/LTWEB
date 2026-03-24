package com.hung.demo_web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaoVe {
    
    @ExceptionHandler(KhongTimThay.class)
    public ResponseEntity<ThongBaoLoi> handleKhongTimThay(KhongTimThay ex){
        ThongBaoLoi error =  new ThongBaoLoi(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(LoiThaoTac.class)
    public ResponseEntity<ThongBaoLoi> handleLoiThaoTac(LoiThaoTac ex){
        ThongBaoLoi error = new ThongBaoLoi(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ThongBaoLoi> handleBaoVe(Exception ex){
        ThongBaoLoi error = new ThongBaoLoi(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Hệ thống đang bảo trì hoặc gặp sự cố"+ ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
