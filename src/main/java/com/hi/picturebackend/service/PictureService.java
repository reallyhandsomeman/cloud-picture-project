package com.hi.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hi.picturebackend.model.dto.picture.PictureQueryRequest;
import com.hi.picturebackend.model.dto.picture.PictureReviewRequest;
import com.hi.picturebackend.model.dto.picture.PictureUploadRequest;
import com.hi.picturebackend.model.entity.Picture;
import com.hi.picturebackend.model.entity.User;
import com.hi.picturebackend.model.vo.PictureVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zouzonglin
 * @description 针对表【picture(图片)】的数据库操作Service
 * @createDate 2026-01-15 15:57:44
 */
public interface PictureService extends IService<Picture> {
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    void validPicture(Picture picture);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);


    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 上传图片
     *
     * @param inputSource
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);
}
