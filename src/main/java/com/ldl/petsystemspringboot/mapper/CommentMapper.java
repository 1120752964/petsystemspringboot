package com.ldl.petsystemspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldl.petsystemspringboot.entity.Comment;
import com.ldl.petsystemspringboot.entity.User;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * description : 添加User
     *
     * @param user 实例对象
     * @return 影响行数
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    int insert(Comment user);

    /**
     * description : 删除User
     *
     * @param  id 主键
     * @return 影响行数
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    int deleteById(Integer id);

    /**
     * description : 通过ID查询单条数据
     *
     * @param  id 主键
     * @return 实例对象
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    User queryById(Integer id);

    /**
     * description : 查询全部数据(分页使用MyBatis的插件实现)
     *
     * @return 对象列表
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    List<User> queryAll();

    /**
     * description : 实体作为筛选条件查询数据
     *
     * @param  user 实例对象
     * @return 对象列表
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    List<User> queryAll(User user);

    /**
     * description : 修改User
     *
     * @param  user 根据user的主键修改数据
     * @return 影响行数
     * @author godfrey
     * @since  2020-04-20 19:21:17
     */
    int update(User user);
}
