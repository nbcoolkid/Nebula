package com.nebula.auth.service;

import com.nebula.auth.dto.response.MenuResponse;
import com.nebula.auth.dto.response.PaginatedResponse;
import com.nebula.auth.dto.response.RoleItem;
import com.nebula.auth.dto.response.UserItem;

import java.util.Map;

public interface SystemManageService {

    PaginatedResponse<UserItem> getUserList(Map<String, Object> params);

    PaginatedResponse<RoleItem> getRoleList(Map<String, Object> params);

    MenuResponse[] getMenuList();
}
