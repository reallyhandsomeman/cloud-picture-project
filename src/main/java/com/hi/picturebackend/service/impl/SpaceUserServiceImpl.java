package com.hi.picturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hi.picturebackend.model.entity.SpaceUser;
import com.hi.picturebackend.service.SpaceUserService;
import com.hi.picturebackend.mapper.SpaceUserMapper;
import org.springframework.stereotype.Service;

/**
* @author zouzonglin
* @description 针对表【space_user(空间用户关联)】的数据库操作Service实现
* @createDate 2026-02-02 14:21:45
*/
@Service
public class SpaceUserServiceImpl extends ServiceImpl<SpaceUserMapper, SpaceUser>
    implements SpaceUserService{

}




