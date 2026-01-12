package com.hi.picturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hi.picturebackend.model.entity.User;
import com.hi.picturebackend.service.UserService;
import com.hi.picturebackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author zouzonglin
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-01-12 14:57:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




