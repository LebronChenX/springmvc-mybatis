package com.lebron.springmvc.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lebron.springmvc.model.BaseBean;

/**
 * 抽取出一些基本的操作
 * 
 * @author Administrator
 */
public abstract class BaseService<T extends BaseBean> {

    @Autowired
    private Mapper<T> mapper;

    public T selectByPrimaryKey(Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有对象
     * 
     * @return
     */
    public List<T> selectAll() {
        return this.mapper.select(null);
    }

    /**
     * 根据条件查询唯一的一条记录
     * 
     * @return
     */
    public T selectOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询list数据
     * 
     * @param record
     * @return
     */
    public List<T> selectList(T record) {
        return this.mapper.select(record);
    }

    /**
     * 根据条件查询分页数据
     * 
     * @param record
     * @return
     */
    public PageInfo<T> selectPageListByExample(T record, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);// 开启分页
        List<T> list = this.mapper.select(record);// 正常查询数据
        PageInfo<T> pageInfo = new PageInfo<T>(list);// 构建page信息
        return pageInfo;
    }

    public Integer insert(T record) {
        // 把创建时间，和修改时间初始化进去。
        record.setCreateTime(System.currentTimeMillis());
        record.setUpdateTime(record.getCreateTime());
        record.setDeleteFlag(0);
        return this.mapper.insert(record);

    }

    /**
     * 修改对象，如果属性是null，也进行修改
     * 
     * @param record
     * @return
     */
    public Integer update(T record) {
        record.setUpdateTime(System.currentTimeMillis());
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 修改对象，如果属性是null，则不进行修改
     * 
     * @param record
     * @return
     */
    public Integer updateSelective(T record) {
        record.setCreateTime(null);// 强制把创建时间设置成null，不让修改
        record.setUpdateTime(System.currentTimeMillis());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    public Integer deleteById(Object id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据主键逻辑删除
     * 
     * @param id
     * @return
     */
    public Integer deleteLogicById(Object id) {
        T record = this.selectByPrimaryKey(id);
        record.setDeleteFlag(1);
        return this.updateSelective(record);
    }

    /**
     * 根据ids 删除多个对象
     * 
     * @param clazz
     *            ，实体对应的class
     * @param property
     *            实体类中主键属性名
     * @param ids
     *            要删除的主键的值
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> ids) {
        Example example = new Example(clazz);
        // 第一个参数，实体类的属性
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除数据
     * 
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }

    /**
     * 根据条件，逻辑删除
     * 
     * @param record
     * @return
     */
    public Integer deleteLogicByWhere(T record) {
        List<T> list = this.selectList(record);
        if (list != null && list.size() > 0) {
            for (T t : list) {
                t.setDeleteFlag(1);
                this.updateSelective(t);
            }
        }
        return list.size();
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
