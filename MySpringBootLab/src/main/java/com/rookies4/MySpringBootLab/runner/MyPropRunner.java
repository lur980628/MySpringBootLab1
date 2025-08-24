// MyPropRunner.java (1-4, 1-7에서 수정 예정)
package com.rookies4.MySpringBootLab.runner;

import com.rookies4.MySpringBootLab.MyEnvironment;
import com.rookies4.MySpringBootLab.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    // 1-4) @Value 어노테이션을 사용하여 환경변수 로드
    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    // 1-5) MyPropProperties 객체 주입 (추가)
    private final MyPropProperties myPropProperties;

    // 1-6) MyEnvironment 객체 주입 (추가)
    private final MyEnvironment myEnvironment;

    // 생성자 주입
    public MyPropRunner(MyPropProperties myPropProperties, MyEnvironment myEnvironment) {
        this.myPropProperties = myPropProperties;
        this.myEnvironment = myEnvironment;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("--- 1-4) @Value를 사용한 환경변수 출력 ---");
        // 1-7에서 logger.info()로 변경
        logger.info("Username (@Value): " + username);
        logger.info("Port (@Value): " + port);

        logger.info("--- 1-5) MyPropProperties(@ConfigurationProperties)를 사용한 환경변수 출력 ---");
        // 1-7에서 logger.debug()로 변경
        // 1-7 요구사항에 따라 logger.debug()로 변경하여 prod 환경에서는 출력되지 않도록 합니다.
        logger.debug("Username (@ConfigurationProperties): " + myPropProperties.getUsername());
        logger.debug("Port (@ConfigurationProperties): " + myPropProperties.getPort());

        logger.info("--- 1-6) @Profile로 주입된 MyEnvironment 출력 ---");
        logger.info("Environment Mode: " + myEnvironment.getMode());
    }
}