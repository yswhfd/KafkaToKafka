package com.asiainfo.dao.scheduler;

import com.asiainfo.entity.scheduler.UsersEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Classname UserDao
 * @Description TODO
 * @Date 2019/9/18 16:33
 * @Created by Jhon_yh
 */
@Repository
public interface UserDao extends PagingAndSortingRepository<UsersEntity, Integer>{
}
