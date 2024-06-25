package sber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.*;
import java.util.stream.Collectors;

public class SberMain {

    private static final String MAIN_URL = "https://megamarket.ru";


    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.edge.driver", "selenium\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();
        Map<String, Double> benefitMap = new HashMap<>();

        driver.get(MAIN_URL);
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
                Element priceElement = productItem.selectFirst("div[data-test='product-price']");

                if (linkElement != null && priceElement != null) {
                    String href = MAIN_URL + linkElement.attr("href");
                    String priceStr = priceElement.text().replaceAll("[^\\d]", "");
                    double price = Double.parseDouble(priceStr);

                    double benefit = Math.round((bonusAmount / price) * 100);
                    benefitMap.put(href, benefit);
                }
            }
        }
        driver.quit();
        sortMap(benefitMap).forEach((href, benefit) -> {
            System.out.println("Ссылка на товар: " + href + " Выгода: " + benefit + "%");
        });
    }
    static Map<String, Double> sortMap(Map<String, Double> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
