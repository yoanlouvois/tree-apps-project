package others;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;


    public Notification(String message){
        this.message = message;
        this.date=LocalDate.now();
        this.time=LocalTime.now();
    }

    public Notification(String message, LocalDate date, LocalTime time){
        this.message = message;
        this.date=date;
        this.time=time;
    }
    public Notification(){
        this.message = "";
        this.date=null;
        this.time=null;
    }

    private String getMessage(){
        return message;
    }
    private LocalDate getDate(){
        return date;
    }
    private LocalTime getTime(){
        return time;
    }
}
