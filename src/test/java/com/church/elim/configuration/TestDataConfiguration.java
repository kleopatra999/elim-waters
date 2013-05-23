package com.church.elim.configuration;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Profile("dev")
@ImportResource({"classpath:/META-INF/spring/applicationContext.xml", 
	"classpath:/META-INF/spring/spring-security.xml",
	"classpath:/META-INF/spring/test-webmvc-config.xml"})
@Configuration
public class TestDataConfiguration {
	@Bean
	public DataSource dataSource() {
        BasicDataSource dataSource =  new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("B33f34t3r");
        dataSource.setUrl("jdbc:mysql://localhost:3306/elimtest");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3);
        dataSource.setNumTestsPerEvictionRun(3);
        dataSource.setMinEvictableIdleTimeMillis(180000);
        dataSource.setValidationQuery("select 1");
        return dataSource;
    }
	
//	@Bean DaoAuthenticationProvider authenticationProvider(){
//		return Mockito.mock(DaoAuthenticationProvider.class);
//	}
//	
//	@Bean UserDetailsService userDetailsService(){
//		return Mockito.mock(UserDetailsService.class);
//	}
}
