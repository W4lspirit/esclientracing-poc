package org.wal.esclientracing.repository;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;

@Repository
public class SomeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomeRepository.class);

    private final RestHighLevelClient restHighLevelClient;

    public SomeRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @NewSpan
    public boolean exists(String id) {
        return extracted(new GetRequest("my_index", id)).isPresent();
    }

    @NewSpan
    private Optional<GetResponse> extracted(GetRequest getRequest) {
        try {
            return Optional.of(restHighLevelClient.get(getRequest, RequestOptions.DEFAULT));
        } catch (IOException e) {
            LOGGER.warn("Failed to execute query", e);
        }
        return Optional.empty();
    }

}
