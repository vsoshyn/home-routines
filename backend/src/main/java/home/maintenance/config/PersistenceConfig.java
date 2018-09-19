package home.maintenance.config;

import home.maintenance.service.CreatedByAware;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.h2.Driver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Buibi on 29.10.2016.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "createdByAware")
@EnableJpaRepositories(basePackages = "home.maintenance.dao.common")
public class PersistenceConfig {

    @Autowired
    private Environment environment;

    @Bean
    public CreatedByAware createdByAware() {
        return new CreatedByAware();
    }

    @Bean("entityManagerFactory")
    @Profile("h2")
    public EntityManagerFactory entityManagerFactoryH2(@Autowired DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = createCommonEMFBean(dataSource);
        bean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "create-drop");
        bean.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    @Profile("h2")
    public DataSource dataSourceH2() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl(environment.getProperty("application.h2.jdbc.url"));
        dataSource.setUsername(environment.getProperty("application.h2.jdbc.user"));
        return dataSource;
    }

    @Bean("entityManagerFactory")
    @Profile("oracle")
    public EntityManagerFactory entityManagerFactoryOracle(@Autowired DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = createCommonEMFBean(dataSource);
        bean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "update");
        bean.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        bean.getJpaPropertyMap().put("hibernate.default_schema", environment.getProperty("application.oracle.jdbc.schema"));

        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    @Profile("oracle")
    public DataSource dataSourceOracle() throws SQLException {
        OracleConnectionPoolDataSource dataSource = new OracleConnectionPoolDataSource();
        dataSource.setURL(environment.getProperty("application.oracle.jdbc.url"));
        dataSource.setUser(environment.getProperty("application.oracle.jdbc.user"));
        dataSource.setPassword(environment.getProperty("application.oracle.jdbc.password"));
        return dataSource;
    }

    @Bean("entityManagerFactory")
    @Profile("postgres")
    public EntityManagerFactory entityManagerFactoryPostgres(@Autowired DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = createCommonEMFBean(dataSource);
        bean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "create");
        bean.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        bean.getJpaPropertyMap().put("hibernate.default_schema", environment.getProperty("application.postgres.jdbc.schema"));

        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    @Profile("postgres")
    public DataSource dataSourcePostgres() throws SQLException {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setURL(environment.getProperty("application.postgres.jdbc.url"));
        dataSource.setUser(environment.getProperty("application.postgres.jdbc.user"));
        dataSource.setPassword(environment.getProperty("application.postgres.jdbc.password"));
        return dataSource;
    }

    @Bean
    public EntityManager entityManager(@Autowired EntityManagerFactory entityManagerFactory) {
        SharedEntityManagerBean bean = new SharedEntityManagerBean();
        bean.setEntityManagerFactory(entityManagerFactory);
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private LocalContainerEntityManagerFactoryBean createCommonEMFBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPersistenceProvider(new HibernatePersistenceProvider());
        bean.setPersistenceUnitName("home-maintenance");
        bean.setPackagesToScan("home.maintenance.model");

        bean.getJpaPropertyMap().put("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql"));
        bean.getJpaPropertyMap().put("hibernate.format_sql", "true");
        return bean;
    }
}
