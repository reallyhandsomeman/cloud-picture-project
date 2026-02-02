package com.hi.picturebackend.test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.hi.picturebackend.manager.auth.model.SpaceUserAuthConfig;
import org.junit.jupiter.api.Test;

public class ReadJsonTest {

    @Test
    public void readJson() {
        SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;
        String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
    }

}
