package com.asiainfo.dao.scheduler;

import com.asiainfo.entity.scheduler.DvBatchBoncToScheLogEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Classname DvBatchBonc2Scheduler
 * @Description TODO
 * @Date 2019/9/18 17:47
 * @Created by Jhon_yh
 */
@Repository
public interface DvBatchBonc2Scheduler extends PagingAndSortingRepository<DvBatchBoncToScheLogEntity, String>{

}
