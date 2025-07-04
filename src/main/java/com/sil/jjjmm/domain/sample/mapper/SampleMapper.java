package com.sil.jjjmm.domain.sample.mapper;

import com.sil.jjjmm.domain.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleMapper {
    List<UserDto.Response> findUserAll();
}
