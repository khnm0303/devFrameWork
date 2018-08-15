package com.lyh.boot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@MapperScan(value={"com.lyh.boot.board.mapper"})
public class DevFrameWorkApplication implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(DevFrameWorkApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DevFrameWorkApplication.class, args);
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
	}
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public void run(String...strings ) throws Exception {
		
		log.info("************************* CREATE tables customers****************************");
		jdbc.execute(" DROP TABLE customers IF EXISTS");
		jdbc.execute(" CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255) ) ");

		List<Object[]> splitUpNames = Arrays.asList("John Woo","Jeff Dean","Josh Bloch","Josh Long")
				.stream().map(name->name.split(" "))
				.collect(Collectors.toList());
		splitUpNames.forEach(name->log.info(String.format(" Inserting customers record for %s %s ", name[0] , name[1])));
		jdbc.batchUpdate("INSERT INTO customers(first_name , last_name) VALUES ( ?,? ) ", splitUpNames);

		
		log.info("************************* CREATE tables****************************");
		jdbc.execute(" DROP TABLE BOARD IF EXISTS ");
		jdbc.execute(" CREATE TABLE BOARD ( bno number, subject VARCHAR(50), content VARCHAR(255), writer varchar2(50), reg_date date) ");
		
		jdbc.update(" insert into board( bno,subject,content,writer,reg_date) values( 0,'subject0','content0','writer0',sysdate) ");
		jdbc.update(" insert into board( bno,subject,content,writer,reg_date) values( 1,'subject1','content1','writer1',sysdate) ");
		jdbc.update(" insert into board( bno,subject,content,writer,reg_date) values( 2,'subject2','content2','writer2',sysdate) ");
		
		
		log.info("************************* CREATE files ****************************");
		jdbc.execute(" DROP TABLE files IF EXISTS ");
		
		jdbc.execute(" create table files( " 
				    +" fno int not null, "
				    +" bno int not null, "
				    +" filename varchar(200) not null, "
				    +" fileOriName varchar(300) not null, "
				    +" fileurl varchar(500) not null) ");
		
		
	}
}
