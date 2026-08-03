package com.ailearn.controller;

import com.ailearn.common.BusinessException;
import com.ailearn.common.Result;
import com.ailearn.entity.User;
import com.ailearn.entity.enums.Role;
import com.ailearn.security.UserContext;
import com.ailearn.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserContext userContext;

    public AuthController(AuthService authService, UserContext userContext) {
        this.authService = authService;
        this.userContext = userContext;
    }

    public record LoginReq(@NotBlank String email, @NotBlank String password) {}
    public record RegisterReq(@NotBlank @Email String email, @NotBlank String name,
                              @NotBlank String password, Role role) {}
    public record ForgotReq(@NotBlank @Email String email) {}
    public record ResetReq(@NotBlank String token, @NotBlank String password) {}
    public record ProfileReq(String name, String bio, String avatar, String phone, String major) {}

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginReq req) {
        return Result.success(authService.login(req.email, req.password));
    }

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterReq req) {
        return Result.success(authService.register(req.email, req.name, req.password, req.role));
    }

    @PostMapping("/forgot-password")
    public Result<String> forgot(@Valid @RequestBody ForgotReq req) {
        authService.forgotPassword(req.email);
        return Result.success("若邮箱存在，重置链接已发送");
    }

    @PostMapping("/reset-password")
    public Result<String> reset(@Valid @RequestBody ResetReq req) {
        authService.resetPassword(req.token, req.password);
        return Result.success("密码已重置");
    }

    @GetMapping("/me")
    public Result<User> me() {
        return Result.success(authService.getProfile(userContext.getUserId()));
    }

    @PutMapping("/me")
    public Result<User> updateMe(@Valid @RequestBody ProfileReq req) {
        User user = authService.updateProfile(userContext.getUserId(),
                req.name(), req.bio(), req.avatar(), req.phone(), req.major());
        return Result.success(user);
    }
}
