package com.asiainfo.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.asiainfo.dao.scheduler"},// 1. dao 层所在的包
        entityManagerFactoryRef = "firstEntityManagerFactory",
        transactionManagerRef = "firstTransactionManager")
@EnableTransactionManagement
public class SchedulerDataSourceConfig {

    @Bean
    @Primary
    public PlatformTransactionManager firstTransactionManager() {
        log.info("scheduler datasource initialized");
        return new JpaTransactionManager(firstEntityManagerFactory().getObject());
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(firstDataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());

        factoryBean.setPackagesToScan("com.asiainfo.entity.scheduler");// 2. 实体类所在的包

        return factoryBean;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.scheduler")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.scheduler.configuration")
    public DataSource firstDataSource() {
        return firstDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class) // 3. 可以显示指定连接池，也可以不显示指定；即此行代码可以注释掉
                .build();
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        return hibernateProperties;
    }

}