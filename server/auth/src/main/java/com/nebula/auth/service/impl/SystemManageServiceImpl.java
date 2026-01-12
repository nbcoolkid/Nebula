package com.nebula.auth.service.impl;

import com.nebula.auth.dto.response.*;
import com.nebula.auth.service.SystemManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SystemManageServiceImpl implements SystemManageService {

    @Override
    public PaginatedResponse<UserItem> getUserList(Map<String, Object> params) {
        log.debug("Fetching user list with params: {}", params);

        List<UserItem> users = new ArrayList<>();

        UserItem user1 = new UserItem();
        user1.setId(1);
        user1.setAvatar("https://ui-avatars.com/api/?name=John+Doe&background=0D8ABC&color=fff");
        user1.setStatus("1");
        user1.setUserName("admin");
        user1.setUserGender("1");
        user1.setNickName("超级管理员");
        user1.setUserPhone("13800138000");
        user1.setUserEmail("admin@nebula.com");
        user1.setUserRoles(Arrays.asList("R_SUPER", "R_ADMIN"));
        user1.setCreateBy("system");
        user1.setCreateTime("2024-01-01 10:00:00");
        user1.setUpdateBy("admin");
        user1.setUpdateTime("2024-01-15 10:00:00");
        users.add(user1);

        UserItem user2 = new UserItem();
        user2.setId(2);
        user2.setAvatar("https://ui-avatars.com/api/?name=Jane+Smith&background=FF6B6B&color=fff");
        user2.setStatus("1");
        user2.setUserName("jane.smith");
        user2.setUserGender("2");
        user2.setNickName("普通用户");
        user2.setUserPhone("13800138001");
        user2.setUserEmail("jane@nebula.com");
        user2.setUserRoles(Arrays.asList("R_USER"));
        user2.setCreateBy("admin");
        user2.setCreateTime("2024-01-05 10:00:00");
        user2.setUpdateBy("admin");
        user2.setUpdateTime("2024-01-10 10:00:00");
        users.add(user2);

        UserItem user3 = new UserItem();
        user3.setId(3);
        user3.setAvatar("https://ui-avatars.com/api/?name=Bob+Johnson&background=4ECDC4&color=fff");
        user3.setStatus("2");
        user3.setUserName("bob.johnson");
        user3.setUserGender("1");
        user3.setNickName("测试用户");
        user3.setUserPhone("13800138002");
        user3.setUserEmail("bob@nebula.com");
        user3.setUserRoles(Arrays.asList("R_USER"));
        user3.setCreateBy("admin");
        user3.setCreateTime("2024-01-08 10:00:00");
        user3.setUpdateBy("admin");
        user3.setUpdateTime("2024-01-12 10:00:00");
        users.add(user3);

        PaginatedResponse<UserItem> response = new PaginatedResponse<>();
        response.setRecords(users);
        response.setCurrent(1);
        response.setSize(10);
        response.setTotal(3);
        return response;
    }

    @Override
    public PaginatedResponse<RoleItem> getRoleList(Map<String, Object> params) {
        log.debug("Fetching role list with params: {}", params);

        List<RoleItem> roles = new ArrayList<>();

        RoleItem role1 = new RoleItem();
        role1.setRoleId(1);
        role1.setRoleName("超级管理员");
        role1.setRoleCode("R_SUPER");
        role1.setDescription("拥有所有权限");
        role1.setEnabled(true);
        role1.setCreateTime("2024-01-01 10:00:00");
        roles.add(role1);

        RoleItem role2 = new RoleItem();
        role2.setRoleId(2);
        role2.setRoleName("管理员");
        role2.setRoleCode("R_ADMIN");
        role2.setDescription("拥有部分管理权限");
        role2.setEnabled(true);
        role2.setCreateTime("2024-01-01 10:00:00");
        roles.add(role2);

        RoleItem role3 = new RoleItem();
        role3.setRoleId(3);
        role3.setRoleName("普通用户");
        role3.setRoleCode("R_USER");
        role3.setDescription("普通用户权限");
        role3.setEnabled(true);
        role3.setCreateTime("2024-01-01 10:00:00");
        roles.add(role3);

        PaginatedResponse<RoleItem> response = new PaginatedResponse<>();
        response.setRecords(roles);
        response.setCurrent(1);
        response.setSize(10);
        response.setTotal(3);
        return response;
    }

    @Override
    public MenuResponse[] getMenuList() {
        log.debug("Fetching menu list");

        MenuMeta.AuthButton menuAuthAdd = new MenuMeta.AuthButton("新增", "add");
        MenuMeta.AuthButton menuAuthEdit = new MenuMeta.AuthButton("编辑", "edit");
        MenuMeta.AuthButton menuAuthDelete = new MenuMeta.AuthButton("删除", "delete");

        MenuResponse dashboardRoute = new MenuResponse();
        dashboardRoute.setPath("/dashboard");
        dashboardRoute.setName("Dashboard");
        dashboardRoute.setComponent("/index/index");
        MenuMeta dashboardMeta = new MenuMeta();
        dashboardMeta.setTitle("menus.home.title");
        dashboardMeta.setIcon("ri:dashboard-3-line");
        dashboardRoute.setMeta(dashboardMeta);
        dashboardRoute.setId(1);

        MenuResponse systemRoute = new MenuResponse();
        systemRoute.setPath("/system");
        systemRoute.setName("System");
        systemRoute.setComponent("/index/index");
        MenuMeta systemMeta = new MenuMeta();
        systemMeta.setTitle("menus.system.title");
        systemMeta.setIcon("ri:user-3-line");
        systemMeta.setRoles(Arrays.asList("R_SUPER", "R_ADMIN"));
        systemRoute.setMeta(systemMeta);
        systemRoute.setId(10);

        List<MenuResponse> systemChildren = new ArrayList<>();

        MenuResponse userRoute = new MenuResponse();
        userRoute.setPath("user");
        userRoute.setName("User");
        userRoute.setComponent("/system/user");
        MenuMeta userMeta = new MenuMeta();
        userMeta.setTitle("menus.system.user");
        userMeta.setIcon("ri:user-line");
        userMeta.setKeepAlive(true);
        userMeta.setRoles(Arrays.asList("R_SUPER", "R_ADMIN"));
        userRoute.setMeta(userMeta);
        userRoute.setId(11);
        systemChildren.add(userRoute);

        MenuResponse roleRoute = new MenuResponse();
        roleRoute.setPath("role");
        roleRoute.setName("Role");
        roleRoute.setComponent("/system/role");
        MenuMeta roleMeta = new MenuMeta();
        roleMeta.setTitle("menus.system.role");
        roleMeta.setIcon("ri:user-settings-line");
        roleMeta.setKeepAlive(true);
        roleMeta.setRoles(Arrays.asList("R_SUPER"));
        roleRoute.setMeta(roleMeta);
        roleRoute.setId(12);
        systemChildren.add(roleRoute);

        MenuResponse userCenterRoute = new MenuResponse();
        userCenterRoute.setPath("user-center");
        userCenterRoute.setName("UserCenter");
        userCenterRoute.setComponent("/system/user-center");
        MenuMeta userCenterMeta = new MenuMeta();
        userCenterMeta.setTitle("menus.system.userCenter");
        userCenterMeta.setIcon("ri:user-line");
        userCenterMeta.setIsHide(true);
        userCenterMeta.setKeepAlive(true);
        userCenterMeta.setIsHideTab(true);
        userCenterRoute.setMeta(userCenterMeta);
        userCenterRoute.setId(13);
        systemChildren.add(userCenterRoute);

        MenuResponse menuRoute = new MenuResponse();
        menuRoute.setPath("menu");
        menuRoute.setName("Menus");
        menuRoute.setComponent("/system/menu");
        MenuMeta menuMeta = new MenuMeta();
        menuMeta.setTitle("menus.system.menu");
        menuMeta.setIcon("ri:menu-line");
        menuMeta.setKeepAlive(true);
        menuMeta.setRoles(Arrays.asList("R_SUPER"));
        menuMeta.setAuthList(Arrays.asList(menuAuthAdd, menuAuthEdit, menuAuthDelete));
        menuRoute.setMeta(menuMeta);
        menuRoute.setId(14);
        systemChildren.add(menuRoute);

        systemRoute.setChildren(systemChildren);

        return new MenuResponse[] { dashboardRoute, systemRoute };
    }
}
