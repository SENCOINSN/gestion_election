package com.sid.gl.utils;

import com.sid.gl.commons.DataResponse;
import org.springframework.data.domain.Page;

import java.util.List;

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
}
