package com.hi.picturebackend.test;

import com.hi.picturebackend.manager.auth.StpKit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class SaTokenTest {

    @Test
    public void testSaToken() {


        // 在当前会话进行 Space 账号登录
        StpKit.SPACE.login(10001);

        try {
            // 检测当前会话是否以 Space 账号登录，并具有 picture:edit 权限
            StpKit.SPACE.checkPermission("picture:edit");
        } catch (Exception e) {
            System.out.println("无权限");
        }
        // 获取当前 Space 会话的 Session 对象，并进行写值操作
        StpKit.SPACE.getSession().set("user", "程序员鱼皮");

    }
}
