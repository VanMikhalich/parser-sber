package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
        /*System.setProperty("webdriver.edge.driver", "selenium\\msedgedriver.exe");
        WebDriver webDriver = new EdgeDriver();
        webDriver.get("https://4pda.to/");
        Document document = Jsoup.parse(webDriver.getPageSource());
        for (int i = 2; i <= 7; i++) {
            System.out.println("Переход к странице " + i);
            Thread.sleep(5000);
            WebElement paginationButton = webDriver.findElement(By.xpath("//*[@id=\"uTah4xRPFw\"]/ul/li[" + i + "]/a"));
            paginationButton.click();
        }
        webDriver.quit();*/

        List<Post> posts = new ArrayList<>();
        Document doc = Jsoup.connect("https://4pda.to/").get();
        System.out.println("Подключение к главной странице...");
        Elements postTitleElements = doc.getElementsByAttributeValue("itemprop", "url");
        //postTitleElements.forEach(postTitleElement -> System.out.println(postTitleElement.attr("title") + " | " + postTitleElement.attr("href")));
        for (Element postTitleElement : postTitleElements) {
            String detailsLink = postTitleElement.attr("href");
            Post post = new Post();
            post.setDetailsLink(detailsLink);
            post.setTitle(postTitleElement.attr("title"));
            System.out.println("Подключение к деталям о посте: " + detailsLink);
            Document postDetailsDoc = Jsoup.connect(detailsLink).get();
            try {
                Element authorElement = postDetailsDoc.getElementsByClass("name").first().child(0);
                post.setAuthor(authorElement.text());
                post.setAuthorDetailsLink(authorElement.attr("href"));
                post.setDateOfCreated(postDetailsDoc.getElementsByClass("date").first().text());
            } catch (NullPointerException e) {
                post.setAuthor("Автор не определен");
                post.setAuthorDetailsLink("Ссылки нет");
                post.setDateOfCreated("Дата не определена");
            }
            posts.add(post);

        }

        posts.forEach(System.out::println);
    }
}
