package com.lishui.tourism.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lishui.tourism.common.exception.BusinessException;
import com.lishui.tourism.common.result.ResultCode;
import com.lishui.tourism.entity.Tag;
import com.lishui.tourism.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagService extends ServiceImpl<TagMapper, Tag> {
    
    /**
     * 创建标签
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createTag(String name, String type) {
        // 检查标签是否已存在
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getName, name);
        
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS);
        }
        
        Tag tag = new Tag();
        tag.setName(name);
        tag.setType(type);
        
        baseMapper.insert(tag);
        log.info("标签创建成功：{}", name);
        return tag.getId();
    }
    
    /**
     * 获取所有标签
     */
    public List<Tag> getAllTags() {
        return baseMapper.selectList(null);
    }
    
    /**
     * 根据类型获取标签
     */
    public List<Tag> getTagsByType(String type) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getType, type);
        return baseMapper.selectList(wrapper);
    }
    
    /**
     * 删除标签
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        baseMapper.deleteById(id);
        log.info("标签删除成功：{}", id);
    }
}
