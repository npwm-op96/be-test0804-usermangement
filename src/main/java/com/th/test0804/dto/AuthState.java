package com.th.test0804.dto;

import com.th.test0804.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthState<T> {
   private String token;
   private User user;
   private T error;
}
