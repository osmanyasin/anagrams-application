package bsg.application.anagrams;

import bsg.application.anagrams.core.WordMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public MapperFactoryBean<WordMapper> wordMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<WordMapper> factoryBean = new MapperFactoryBean<>(WordMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
}
