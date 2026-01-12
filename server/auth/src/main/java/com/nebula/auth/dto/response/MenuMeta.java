package com.nebula.auth.dto.response;

import java.util.List;

public class MenuMeta {

    private String title;

    private String icon;

    private Boolean showBadge;

    private String showTextBadge;

    private Boolean isHide;

    private Boolean isHideTab;

    private String link;

    private Boolean isIframe;

    private Boolean keepAlive;

    private List<AuthButton> authList;

    private Boolean isFirstLevel;

    private List<String> roles;

    private Boolean fixedTab;

    private String activePath;

    private Boolean isFullPage;

    private Boolean isAuthButton;

    private String authMark;

    private String parentPath;

    public MenuMeta() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getShowBadge() {
        return showBadge;
    }

    public void setShowBadge(Boolean showBadge) {
        this.showBadge = showBadge;
    }

    public String getShowTextBadge() {
        return showTextBadge;
    }

    public void setShowTextBadge(String showTextBadge) {
        this.showTextBadge = showTextBadge;
    }

    public Boolean getIsHide() {
        return isHide;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public Boolean getIsHideTab() {
        return isHideTab;
    }

    public void setIsHideTab(Boolean isHideTab) {
        this.isHideTab = isHideTab;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getIsIframe() {
        return isIframe;
    }

    public void setIsIframe(Boolean isIframe) {
        this.isIframe = isIframe;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public List<AuthButton> getAuthList() {
        return authList;
    }

    public void setAuthList(List<AuthButton> authList) {
        this.authList = authList;
    }

    public Boolean getIsFirstLevel() {
        return isFirstLevel;
    }

    public void setIsFirstLevel(Boolean isFirstLevel) {
        this.isFirstLevel = isFirstLevel;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean getFixedTab() {
        return fixedTab;
    }

    public void setFixedTab(Boolean fixedTab) {
        this.fixedTab = fixedTab;
    }

    public String getActivePath() {
        return activePath;
    }

    public void setActivePath(String activePath) {
        this.activePath = activePath;
    }

    public Boolean getIsFullPage() {
        return isFullPage;
    }

    public void setIsFullPage(Boolean isFullPage) {
        this.isFullPage = isFullPage;
    }

    public Boolean getIsAuthButton() {
        return isAuthButton;
    }

    public void setIsAuthButton(Boolean isAuthButton) {
        this.isAuthButton = isAuthButton;
    }

    public String getAuthMark() {
        return authMark;
    }

    public void setAuthMark(String authMark) {
        this.authMark = authMark;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public static class AuthButton {
        private String title;
        private String authMark;

        public AuthButton() {
        }

        public AuthButton(String title, String authMark) {
            this.title = title;
            this.authMark = authMark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthMark() {
            return authMark;
        }

        public void setAuthMark(String authMark) {
            this.authMark = authMark;
        }
    }
}
