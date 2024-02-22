package com.planbow.idp.datasource.util;


import com.planbow.util.data.support.configurations.hibernate.DatasourcePoolConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "data-sources.idp.pool-configuration")
public class DataSourcePoolConfig extends DatasourcePoolConfiguration {

}
