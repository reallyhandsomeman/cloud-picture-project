package com.hi.picturebackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hi.picturebackend.exception.BusinessException;
import com.hi.picturebackend.exception.ErrorCode;
import com.hi.picturebackend.exception.ThrowUtils;
import com.hi.picturebackend.mapper.SpaceMapper;
import com.hi.picturebackend.model.dto.space.SpaceAddRequest;
import com.hi.picturebackend.model.entity.Space;
import com.hi.picturebackend.model.entity.User;
import com.hi.picturebackend.model.enums.SpaceLevelEnum;
import com.hi.picturebackend.service.SpaceService;
import com.hi.picturebackend.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author zouzonglin
 * @description 针对表【space(空间)】的数据库操作Service实现
 * @createDate 2026-01-27 15:23:41
 */
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
        implements SpaceService {

    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);
        // 要创建
        if (add) {
            if (StrUtil.isBlank(spaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称不能为空");
            }
            if (spaceLevel == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不能为空");
            }
        }
        // 修改数据时，如果要改空间级别
        if (spaceLevel != null && spaceLevelEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不存在");
        }
        if (StrUtil.isNotBlank(spaceName) && spaceName.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称过长");
        }
    }

    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        // 根据空间级别，自动填充限额
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(space.getSpaceLevel());
        if (spaceLevelEnum != null) {
            long maxSize = spaceLevelEnum.getMaxSize();
            if (space.getMaxSize() == null) {
                space.setMaxSize(maxSize);
            }
            long maxCount = spaceLevelEnum.getMaxCount();
            if (space.getMaxCount() == null) {
                space.setMaxCount(maxCount);
            }
        }
    }

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public long addSpace(SpaceAddRequest spaceAddRequest, User loginUser) {

        // 1️⃣ DTO → 实体类转换
        // 将前端传入的 SpaceAddRequest 中的同名属性拷贝到 Space 实体中
        Space space = new Space();
        BeanUtils.copyProperties(spaceAddRequest, space);

        // 2️⃣ 处理默认值（防止前端未传导致空值问题）
        // 空间名称为空时，设置默认名称
        if (StrUtil.isBlank(spaceAddRequest.getSpaceName())) {
            space.setSpaceName("默认空间");
        }
        // 空间级别未指定时，默认设置为普通空间
        if (spaceAddRequest.getSpaceLevel() == null) {
            space.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());
        }

        // 3️⃣ 根据空间级别填充其他业务相关字段
        // 如容量大小、空间配额等
        this.fillSpaceBySpaceLevel(space);

        // 4️⃣ 数据合法性校验
        // true 表示创建场景下的校验
        this.validSpace(space, true);

        // 5️⃣ 设置空间所属用户
        Long userId = loginUser.getId();
        space.setUserId(userId);

        // 6️⃣ 权限校验
        // 只有管理员才允许创建非普通级别的空间
        if (SpaceLevelEnum.COMMON.getValue() != spaceAddRequest.getSpaceLevel()
                && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限创建指定级别的空间");
        }

        // 7️⃣ 针对同一用户加锁，防止并发创建多个私有空间
        // 使用 userId 作为锁的粒度，不影响不同用户的并发操作
        RLock lock = redissonClient.getLock("space:create:" + userId);
        lock.lock(30, TimeUnit.SECONDS); // 指定超时时间
        try {
            // 8️⃣ 在事务中执行“是否已存在空间 + 创建空间”的原子操作
            Long newSpaceId = transactionTemplate.execute(status -> {
                // 8.1 校验该用户是否已经存在空间
                // 防止同一用户拥有多个私有空间
                boolean exists = this.lambdaQuery()
                        .eq(Space::getUserId, userId)
                        .exists();
                ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每个用户仅能有一个私有空间");

                // 8.2 写入数据库
                boolean result = this.save(space);
                ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

                // 8.3 返回新创建空间的主键 id
                return space.getId();
            });

            // 9️⃣ 事务返回值是包装类型，防止出现 null
            // 若事务执行失败，返回 -1 作为兜底值
            return Optional.ofNullable(newSpaceId).orElse(-1L);
        } finally {
            lock.unlock();
        }
    }
}




