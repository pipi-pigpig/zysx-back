package com.nurturing.Service;

import com.nurturing.entity.DataVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DataService {


    DataVO getByIdWithData(Long userId);
}
