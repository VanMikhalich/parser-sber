package sber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.*;
import java.util.stream.Collectors;

public class SberPages {

    private static final String NOTEBOOK = "https://megamarket.ru/catalog/noutbuki/page-";
    private static final String IPHONE = "https://megamarket.ru/catalog/smartfony-apple/page-";


    private static final String MAIN_URL = "https://megamarket.ru";
    private static final String URL = "https://megamarket.ru/catalog/smartfony-apple/page-";
    private static final int COUNT_OF_PAGES = 2;


    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.edge.driver", "selenium\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        Map<String, Product> benefitMap = new HashMap<>();

        for (int i = 1; i < COUNT_OF_PAGES; i++) {

            driver.get(URL + i);
            Thread.sleep(5000);

            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            Elements cashbacks = doc.getElementsByAttributeValue("data-test", "bonus-amount");

            for (Element cashback : cashbacks) {
                String bonusAmountStr = cashback.text().replaceAll("[^\\d]", "");
                int bonusAmount = Integer.parseInt(bonusAmountStr);

                Element productItem = cashback.closest("[data-test='product-item']");
                if (productItem != null) {
                    Element linkElement = productItem.selectFirst("a[data-test='product-image-link']");
                    Element priceElement = productItem.selectFirst("span[data-test='product-price']");

                    if (linkElement != null && priceElement != null) {
                        String href = MAIN_URL + linkElement.attr("href");
                        String priceStr = priceElement.text().replaceAll("[^\\d]", "");

                        Product product = new Product();

                        double price = Double.parseDouble(priceStr);
                        product.setPrice(price);

                        double benefit = Math.round((bonusAmount / price) * 100);
                        product.setBenefit(benefit);
                        benefitMap.put(href, product);
                    }
                }
            }
        }
        driver.quit();
        sortMap(benefitMap).forEach((href, product) -> {
            System.out.println("Ссылка на товар: " + href + "; Выгода: " + product.getBenefit() + "%; " + "Цена: " + product.getPrice() + "₽");
        });
    }
    static Map<String, Product> sortMap(Map<String, Product> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Product>comparingByValue(Comparator.comparing(Product::getBenefit).reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
