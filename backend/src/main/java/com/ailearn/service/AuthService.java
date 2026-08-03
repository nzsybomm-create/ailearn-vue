package com.ailearn.service;

import com.ailearn.common.BusinessException;
import com.ailearn.entity.Account;
import com.ailearn.entity.PasswordReset;
import com.ailearn.entity.User;
import com.ailearn.entity.enums.Role;
import com.ailearn.mapper.AccountMapper;
import com.ailearn.mapper.PasswordResetMapper;
import com.ailearn.mapper.UserMapper;
import com.ailearn.security.JwtUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final PasswordResetMapper passwordResetMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper, AccountMapper accountMapper,
                       PasswordResetMapper passwordResetMapper,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.passwordResetMapper = passwordResetMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> login(String email, String password) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }
        Account account = accountMapper.selectOne(Wrappers.<Account>lambdaQuery()
                .eq(Account::getUserId, user.getId()).isNotNull(Account::getPasswordHash));
        if (account == null || !passwordEncoder.matches(password, account.getPasswordHash())) {
            throw new BusinessException(400, "密码错误");
        }
        return buildToken(user);
    }

    private Map<String, Object> buildToken(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    public User register(String email, String name, String password, Role role) {
        if (userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email)) != null) {
            throw new BusinessException(400, "邮箱已注册");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role != null ? role : Role.STUDENT);
        userMapper.insert(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setProvider("credentials");
        account.setPasswordHash(passwordEncoder.encode(password));
        accountMapper.insert(account);
        return user;
    }

    public void forgotPassword(String email) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (user == null) {
            // 出于安全不提示
            return;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        PasswordReset pr = new PasswordReset();
        pr.setEmail(email);
        pr.setToken(token);
        pr.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordResetMapper.insert(pr);
        // TODO: 实际发送邮件，此处仅记录 token
    }

    public void resetPassword(String token, String password) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(password)) {
            throw new BusinessException(400, "参数缺失");
        }
        PasswordReset pr = passwordResetMapper.selectOne(Wrappers.<PasswordReset>lambdaQuery()
                .eq(PasswordReset::getToken, token));
        if (pr == null || pr.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(400, "重置链接无效或已过期");
        }
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, pr.getEmail()));
        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }
        Account account = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getUserId, user.getId()));
        if (account == null) {
            account = new Account();
            account.setUserId(user.getId());
            account.setProvider("credentials");
        }
        account.setPasswordHash(passwordEncoder.encode(password));
        accountMapper.insertOrUpdate(account);
        passwordResetMapper.deleteById(pr.getId());
    }

    public User getProfile(Long userId) {
        return userMapper.selectById(userId);
    }

    public User updateProfile(Long userId, String name, String bio, String avatar, String phone, String major) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }
        if (StringUtils.hasText(name)) user.setName(name);
        if (StringUtils.hasText(bio)) user.setBio(bio);
        if (StringUtils.hasText(avatar)) user.setAvatar(avatar);
        if (StringUtils.hasText(phone)) user.setPhone(phone);
        if (StringUtils.hasText(major)) user.setMajor(major);
        userMapper.updateById(user);
        return user;
    }
}
