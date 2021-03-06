package config;

import org.apache.logging.log4j.Logger;
import org.hibernate.jpa.HibernatePersistenceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_STATEMENTS;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@ComponentScan(basePackages = {"dao", "service", "config", "security"})
public class AppConfig {


    @Autowired
    private Environment env;

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(AppConfig.class);

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        //ds.setDriverClassName(JDBCDriver.class.getCanonicalName());
        ds.setDriverClassName(env.getProperty("postgresql.driver"));
        ds.setUrl(env.getProperty("postgresql.jdbcUrl"));
        ds.setUsername(env.getProperty("postgresql.username"));
        ds.setPassword(env.getProperty("postgresql.password"));
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setPackagesToScan("model");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        //properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.setProperty("spring.jpa.show-sql", env.getProperty("spring.jpa.show-sql"));

        properties.setProperty("logging.level.org.hibernate.SQL", env.getProperty("logging.level.org.hibernate.SQL"));
        properties.setProperty("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", env.getProperty("logging.level.org.hibernate.type.descriptor.sql.BasicBinder"));

        // Setting C3P0 properties
        properties.setProperty(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
        properties.setProperty(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
        properties.setProperty(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
        properties.setProperty(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
        properties.setProperty(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));

        return properties;
    }

    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();

        sender.setHost(env.getProperty("spring.mail.host"));               //for gmail use smtp.gmail.com
        sender.setPort(587);
        sender.setUsername(env.getProperty("spring.mail.username"));
        sender.setPassword(env.getProperty("spring.mail.password"));
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.port", "465");
        mailProperties.put("mail.smtp.socketFactory.port", "465");
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.fallback", "false");
        mailProperties.put("mail.debug", "false");      //When true, prints out everything on screen
        sender.setJavaMailProperties(mailProperties);
        return sender;
    }


    @Bean(name = "messageSource")
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:ValidationMessages");
        bean.setDefaultEncoding("UTF-8");
        return bean;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator()
    {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    public Validator getValidator()
    {
        return validator();
    }

}
