package com.asiainfo.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = {"com.asiainfo.dao.zq"},
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager")
@EnableTransactionManagement
public class ZQDataSourceConfig {

    @Bean
    public PlatformTransactionManager secondTransactionManager() {
        log.info("zq datasource initialized");
        return new JpaTransactionManager(secondEntityManagerFactory().getObject());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(secondDataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());

        factoryBean.setPackagesToScan("com.asiainfo.entity.zq");// 2. 实体类所在的包

        return factoryBean;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.zq")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.zq.configuration")
    public DataSource secondDataSource() {
        return secondDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        return hibernateProperties;
    }

}