package com.sil.jjjmm.domain.user.service;

import com.sil.jjjmm.domain.user.dto.UserDto;
import com.sil.jjjmm.domain.user.entity.Users;
import com.sil.jjjmm.domain.user.repository.UserRepository;
import com.sil.jjjmm.global.exception.CustomException;
import com.sil.jjjmm.global.response.ResponseCode;
import com.sil.jjjmm.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<UserDto.Response> userList(UserDto.Search search) {
        List<Users> users = userRepository.findAll();

        // 📦 페이징 + 정렬
        Sort.Direction direction = search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, search.getSortBy()));

        // DTO 변환
        List<UserDto.Response> content = users.stream()
                .map(UserDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, 10);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public UserDto.Response userDetail(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        return UserDto.Response.toDto(user);
    }

    /**
     * 등록
     * @param request
     * @return
     */
    @Transactional
    public UserDto.Response userCreate(UserDto.CreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(utilMessage.getMessage("duplicate.username", null));
        }

        // 엔티티로 변환하기 전에 비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Users user = userRepository.save(request.toEntity());
        return UserDto.Response.toDto(user);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    @Transactional
    public UserDto.Response userModify(String id, UserDto.ModifyRequest request) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        request.userModify(user);

        // MongoDB에 명시적으로 저장
        userRepository.save(user);
        return UserDto.Response.toDto(user);
    }

    /**
     * 삭제
     * @param id
     */
    @Transactional
    public void userDelete(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        userRepository.deleteById(user.getId());
    }
}