package org.example.elasticsearch;

import lombok.Data;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
@Data
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private String hostAndPort; // 127.0.0.1:9200

    @Override
    public RestHighLevelClient elasticsearchClient() {


        // 需要引入 spring-boot-starter-web
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
//                .connectedTo("127.0.0.1:9200", "192.168.38.128:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}

