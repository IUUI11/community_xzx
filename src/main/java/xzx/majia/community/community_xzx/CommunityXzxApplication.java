package xzx.majia.community.community_xzx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xzx.majia.community.community_xzx.mapper")

public class CommunityXzxApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityXzxApplication.class, args);
    }

}
