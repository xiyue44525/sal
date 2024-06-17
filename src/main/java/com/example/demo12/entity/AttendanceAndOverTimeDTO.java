package com.example.demo12.entity;

public class AttendanceAndOverTimeDTO {
    private Attendance attendance;
    private OverTime overTime;

    // 构造器
    public AttendanceAndOverTimeDTO(Attendance attendance, OverTime overTime) {
        this.attendance = attendance;
        this.overTime = overTime;
    }

    // Getter和Setter方法
    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public OverTime getOverTime() {
        return overTime;
    }

    public void setOverTime(OverTime overTime) {
        this.overTime = overTime;
    }
}
