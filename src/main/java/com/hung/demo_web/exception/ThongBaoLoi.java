package com.hung.demo_web.exception;

import java.time.LocalDateTime;

public class ThongBaoLoi {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    public ThongBaoLoi(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
    public void setTimeStamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}
