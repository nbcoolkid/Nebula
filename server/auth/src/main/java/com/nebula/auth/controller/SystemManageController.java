package com.nebula.auth.controller;

import com.nebula.auth.dto.response.MenuResponse;
import com.nebula.auth.dto.response.PaginatedResponse;
import com.nebula.auth.dto.response.RoleItem;
import com.nebula.auth.dto.response.UserItem;
import com.nebula.auth.service.SystemManageService;
import com.nebula.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SystemManageController {

    private final SystemManageService systemManageService;

    @GetMapping("/user/list")
    public Result<PaginatedResponse<UserItem>> getUserList(@RequestParam Map<String, Object> params) {
        log.info("Fetching user list with params: {}", params);
        PaginatedResponse<UserItem> response = systemManageService.getUserList(params);
        return Result.success(response);
    }

    @GetMapping("/role/list")
    public Result<PaginatedResponse<RoleItem>> getRoleList(@RequestParam Map<String, Object> params) {
        log.info("Fetching role list with params: {}", params);
        PaginatedResponse<RoleItem> response = systemManageService.getRoleList(params);
        return Result.success(response);
    }

    @GetMapping("/v3/system/menus")
    public Result<MenuResponse[]> getMenuList() {
        log.info("Fetching menu list");
        MenuResponse[] response = systemManageService.getMenuList();
        return Result.success(response);
    }
}
