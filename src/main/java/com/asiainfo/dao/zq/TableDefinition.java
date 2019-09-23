package com.asiainfo.dao.zq;

import com.asiainfo.entity.zq.TableDefinitionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Classname TableDefinition
 * @Description TODO
 * @Date 2019/9/19 14:32
 * @Created by Jhon_yh
 */
@Repository
public interface TableDefinition extends PagingAndSortingRepository<TableDefinitionEntity, String> {

    TableDefinitionEntity findByTablename(@Param("tablename") String table);

}
