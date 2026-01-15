package com.hi.picturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hi.picturebackend.model.entity.Picture;
import com.hi.picturebackend.service.PictureService;
import com.hi.picturebackend.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author zouzonglin
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2026-01-15 15:57:44
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




