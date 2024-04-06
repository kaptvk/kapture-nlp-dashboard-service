package com.kapturecrm.nlpqueryengineservice.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.kapturecrm.*"})
public class SpringBootConfiguration {


    @Value("${spring.main.datasource.driver-class-name}")
    private String mainDbDriverClassName;

    @Value("${spring.main.datasource.url}")
    private String mainDbUrl;

    @Value("${spring.main.datasource.username}")
    private String mainDbUsername;

    @Value("${spring.main.datasource.password}")
    private String mainDbPassword;

    @Value("${spring.main.hibernate.dialect}")
    private String mainDbDialect;

    @Value("${spring.main.hibernate.ddl-auto}")
    private String mainDbDllAuto;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(mainDbDriverClassName);
        hikariConfig.setJdbcUrl(mainDbUrl);
        hikariConfig.setUsername(mainDbUsername);
        hikariConfig.setPassword(mainDbPassword);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springMicroservice");

        hikariConfig.setConnectionTimeout(60000);
        hikariConfig.setMinimumIdle(5);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return dataSource;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.scanPackages("com.kapturecrm.*", "com.kapture.*");
        return sessionBuilder.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;
    }
}
