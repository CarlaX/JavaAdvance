package com.fzw.mystarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@ConfigurationProperties(prefix = "starterdemo.starter")
public class StarterDemoConfig {
    private Boolean enabled;
    private String username;
    private String password;
    private String nickname;

    public StarterDemoConfig() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "StarterDemoConfig{" +
                "enabled=" + enabled +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
