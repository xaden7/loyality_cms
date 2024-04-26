package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.stock.StockByBranchDTO;
import md.akdev.loyality_cms.dto.stock.StockDTO;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StockService {

    @Value("${spring.elasticdatasource.url}")
    String url;
    @Value("${spring.elasticdatasource.ipAddress}")
    String ipAddress;
    @Value("${spring.elasticdatasource.port}")
    int port;

    private final Logger logger = LoggerFactory.getLogger(StockService.class);

    @SuppressWarnings("unchecked")
    public <T> T findByNameContaining(String name) {

        List<StockDTO> stockDTOList = new ArrayList<>();

        try {
            if (NetworkUtils.sourceIsAvailable(ipAddress, port)) {
                RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                RestTemplate restTemplate = restTemplateBuilder.build();

                StockDTO[] stocks = restTemplate.getForObject(url + "/api/stock/native_find_by_name?name=" + name, StockDTO[].class);

                assert stocks != null;

                stockDTOList = new ArrayList<>(Arrays.asList(stocks).subList(0, Objects.requireNonNull(stocks).length));
            } else {
                stockDTOList = new ArrayList<>();
                logger.error("Load failed network{} is unavailable", ipAddress);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return (T) stockDTOList;
    }

    @SuppressWarnings("unchecked")
    public <T> T findByArticleInBranch(String article) {

        List<StockByBranchDTO> stockByBranchDTOS = new ArrayList<>();

        try {
            if (NetworkUtils.sourceIsAvailable(ipAddress, port)) {
                RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                RestTemplate restTemplate = restTemplateBuilder.build();

                StockByBranchDTO[] stocksByArticleInBranch = restTemplate.getForObject(url + "/api/stock/find_by_article_in_branch?article=" + article, StockByBranchDTO[].class);

                assert stocksByArticleInBranch != null;

                stockByBranchDTOS = new ArrayList<>(Arrays.asList(stocksByArticleInBranch).subList(0, Objects.requireNonNull(stocksByArticleInBranch).length));
            } else {
                stockByBranchDTOS = new ArrayList<>();
                logger.error("Load failed, network{} is unavailable", ipAddress);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return (T) stockByBranchDTOS;
    }
}

