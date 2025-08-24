package com.rookies4.MySpringBootLab;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// myprop. 접두사로 시작하는 모든 속성을 이 클래스에 바인딩
@Component
@ConfigurationProperties(prefix = "myprop")
public class MyPropProperties {

    private String username;
    private int port;

    // Getter (MyPropRunner에서 사용)
    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    // Setter (ConfigurationProperties 바인딩을 위해 필요)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPort(int port) {
        this.port = port;
    }
}