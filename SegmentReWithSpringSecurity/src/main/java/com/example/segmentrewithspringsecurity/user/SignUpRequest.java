package com.example.segmentrewithspringsecurity.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignUpRequest {
  private final String loginId;
  private final String password;
  private final boolean isAdmin;

  public SignUpRequest(String loginId, String password, boolean isAdmin) {
    this.loginId = loginId;
    this.password = password;
    this.isAdmin = isAdmin;
  }
}
