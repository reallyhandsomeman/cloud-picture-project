package com.hi.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hi.picturebackend.model.dto.space.SpaceAddRequest;
import com.hi.picturebackend.model.dto.space.SpaceQueryRequest;
import com.hi.picturebackend.model.entity.Space;
import com.hi.picturebackend.model.entity.User;
import com.hi.picturebackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zouzonglin
 * @description 针对表【space(空间)】的数据库操作Service
 * @createDate 2026-01-27 15:23:41
 */
public interface SpaceService extends IService<Space> {

    void validSpace(Space space, boolean add);

    void fillSpaceBySpaceLevel(Space space);

    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    SpaceVO getSpaceVO(Space space, HttpServletRequest request);
}
