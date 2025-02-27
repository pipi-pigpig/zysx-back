package com.nurturing.Service;

import com.nurturing.entity.DataVO;
import org.springframework.beans.factory.annotation.Autowired;

public interface DataService {


    DataVO getByIdWithData(long userId);

}
