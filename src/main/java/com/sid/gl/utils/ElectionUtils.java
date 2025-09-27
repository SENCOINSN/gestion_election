package com.sid.gl.utils;

import com.sid.gl.commons.DataResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ElectionUtils {

    public static <T,R> DataResponse buildDataResponse(List<R> responses, Page<T> listPage){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setContent(responses);
        dataResponse.setPageNo(listPage.getNumber());
        dataResponse.setPageSize(listPage.getSize());
        dataResponse.setTotalElements(listPage.getTotalElements());
        dataResponse.setTotalPages(listPage.getTotalPages());
        dataResponse.setLast(listPage.isLast());
        return dataResponse;
    }

    // convert date en LocalDateTime
    // le calcul de la date de fin de l'election en specifiant le nombre d'heures

    private static LocalDateTime convertDateToLocalDateTime(Date date, int hours, int minute){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = LocalTime.of(hours, minute,0,0);
        return LocalDateTime.of(localDate, localTime); //yyyy-MM-dd HH:mm:ss
    }

    public static LocalDateTime getElectionEndDate(Date startDate,int startHour, int duration){
        LocalDateTime startLocalDateTime = convertDateToLocalDateTime(startDate,startHour,0);
        return startLocalDateTime.plusHours(duration);
    }
}
