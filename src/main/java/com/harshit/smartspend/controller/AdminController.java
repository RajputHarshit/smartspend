package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.PagedResponse;
import com.harshit.smartspend.dto.UserResponseDto;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.UserRepository;
import com.harshit.smartspend.util.PaginationConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<PagedResponse<UserResponseDto>> getAllUser(@RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
                                                                     @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
                                                                     @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
                                                                     @RequestParam(defaultValue = PaginationConstants.DEFAULT_DIRECTION) String direction){
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<User> userPage = userRepository.findAll(PageRequest.of(page,size,sort));
        List<UserResponseDto> responseDtos=  userPage.stream()
                .map(this::maptoUserResponseDto)
                .collect(Collectors.toList());
        PagedResponse<UserResponseDto> response = new PagedResponse<>(
                responseDtos,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    UserResponseDto maptoUserResponseDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .build();
    }
}
