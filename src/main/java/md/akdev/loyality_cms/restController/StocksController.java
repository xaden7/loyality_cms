package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StocksController {

    private final StockService stockService;

    @GetMapping("/native_find_by_name")
    public <T> T findByStockNameNative(@RequestParam("name")String name) {
        return stockService.findByNameContaining(name);
    }

    @GetMapping("/find_by_article_in_branch")
    public <T> T findByArticle(@RequestParam("article") String article) {
        return stockService.findByArticleInBranch(article);
    }

}
