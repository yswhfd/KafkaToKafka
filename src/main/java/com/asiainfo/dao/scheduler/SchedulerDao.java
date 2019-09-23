package com.asiainfo.dao.scheduler;

import com.asiainfo.entity.scheduler.ProvTableEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @Classname SchedulerDao
 * @Description TODO
 * @Date 2019/9/17 18:12
 * @Created by Jhon_yh
 */
@Repository
public interface SchedulerDao extends PagingAndSortingRepository<ProvTableEntity, String> {

    Optional<ProvTableEntity> findById(@Param("prov_id") String prov_id);

}
