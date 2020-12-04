package com.web.blog.service.Crawling;

import static org.junit.Assume.assumeNoException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;

import com.web.blog.dto.crawling.crawling;
import com.web.blog.dto.crawling.crawlingWrapper;
import com.web.blog.dto.statistic.statiscPostLikeWrapperClass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumCrawling {

    private WebDriver driver;

    private final static String DIRVER_ID = "webdriver.chrome.driver";
    private final static String DIRVER_PATH = "../chromedriver.exe";
    private final static String AwesomeDevBlogURL = "https://awesome-devblog.netlify.app/";
    private final static String DaliyDevBlogURL = "http://daily-devblog.com/log/view";

    public crawlingWrapper[] DoCrawling() {
        Path relativePath = Paths.get("");
        String localpath = relativePath.toAbsolutePath().toString() + "/chromedriver.exe";
        String serverpath = relativePath.toAbsolutePath().toString() + "/s03p13a610/chromedriver";
        if (relativePath.toAbsolutePath().toString().contains("ubuntu")) {
            localpath = serverpath;
        }
        System.setProperty(DIRVER_ID, localpath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);
        crawlingWrapper Wrappers[] = null;
        try {
            driver.get(AwesomeDevBlogURL);
            // data loading을 기다림

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            Wrappers = new crawlingWrapper[5];
            for (int i = 0; i < 5; i++) {
                Wrappers[i] = new crawlingWrapper();
            }
            Wrappers[0] = firstCrawling();
            Wrappers[1] = secondCrawling();
            Wrappers[2] = thirdCrawling();
            Wrappers[3] = fourCrawling();
            Wrappers[4] = lastCrawling();
            // 주간 인기 블로그 크롤링

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
        return Wrappers;
    }

    public String GetString(String xpath) {
        WebElement Element = driver.findElement(By.xpath(xpath));
        String text = Element.getText();
        return text;
    }

    public crawlingWrapper firstCrawling() {
        String weeklyPopularBlogHeaderTitle = GetString(
                "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[1]/article/a");

        crawlingWrapper Wrapper = new crawlingWrapper();
        Wrapper.setHeader(weeklyPopularBlogHeaderTitle);
        System.out.println(weeklyPopularBlogHeaderTitle + " 크롤링 시작");
        List<crawling> crawlinglist = new ArrayList<>();
        int middle_index = 2;
        int tail_index = 0;
        for (int i = 1; i < 11; i++) {
            try {
                if (i >= 6) {
                    tail_index = i - 5;
                    middle_index = 3;
                } else {
                    tail_index++;
                }
                String weeklyPopularPost = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[1]/article/div/div["
                        + middle_index + "]/ul/li[" + tail_index + "]/article/div[1]/div/span/a/small";

                crawling crawled_data = new crawling();
                crawled_data.setTitle(GetString(weeklyPopularPost));

                String weeklyPopularWriter = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[1]/article/div/div["
                        + middle_index + "]/ul/li[" + tail_index + "]/article/div[2]/small";
                crawled_data.setAuthor(GetString(weeklyPopularWriter));
                /*
                 * href 가져오기 WebElement link = driver.findElement(By.tagName("a")) String url =
                 * link.getAttribute("href")
                 */
                String linkstring = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[1]/article/div/div["
                        + middle_index + "]/ul/li[" + tail_index + "]/article/div[1]/div/span/a";
                WebElement link = driver.findElement(By.xpath(linkstring));
                crawled_data.setURL(link.getAttribute("href"));
                crawlinglist.add(crawled_data);
            } catch (Exception e) {
                break;
            }
        }
        Wrapper.setCrawlingList(crawlinglist);
        return Wrapper;
    }

    public crawlingWrapper secondCrawling() {
        String PopularPersonalBlogHeaderTitle = GetString(
                "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/a");

        crawlingWrapper Wrapper = new crawlingWrapper();
        Wrapper.setHeader(PopularPersonalBlogHeaderTitle);
        System.out.println(PopularPersonalBlogHeaderTitle + " 크롤링 시작");
        List<crawling> crawlinglist = new ArrayList<>();
        int middle_index = 2;
        int tail_index = 0;

        // *[@id="app"]/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li[20]/article/div[1]/div/span/a/small
        for (int i = 1; i <= 10; i++) {
            try {
                String PopularPersonalPost = "// *[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a/small";

                crawling crawled_data = new crawling();
                crawled_data.setTitle(GetString(PopularPersonalPost));

                String PopularPersonalPostWriter = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li["
                        + i + "]/article/div[2]/small";
                crawled_data.setAuthor(GetString(PopularPersonalPostWriter));
                /*
                 * href 가져오기 WebElement link = driver.findElement(By.tagName("a")) String url =
                 * link.getAttribute("href")
                 */
                // *[@id="app"]/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li[14]/article/div[1]/div/span/a
                String linkstring = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a";
                WebElement link = driver.findElement(By.xpath(linkstring));
                crawled_data.setURL(link.getAttribute("href"));
                crawlinglist.add(crawled_data);
            } catch (Exception e) {
                break;
            }
        }
        Wrapper.setCrawlingList(crawlinglist);
        return Wrapper;
    }

    public crawlingWrapper thirdCrawling() {
        String PopularTeamlBlogHeaderTitle = GetString(
                "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[2]/article/a");

        crawlingWrapper Wrapper = new crawlingWrapper();
        Wrapper.setHeader(PopularTeamlBlogHeaderTitle);
        System.out.println(PopularTeamlBlogHeaderTitle + " 크롤링 시작");
        List<crawling> crawlinglist = new ArrayList<>();
        int middle_index = 2;
        int tail_index = 0;

        for (int i = 1; i <= 10; i++) {
            try {
                // *[@id="app"]/div/div/article/div/div[2]/div/div[1]/div[2]/div[2]/article/div/ul/li[15]/article/div[1]/div/span/a/small
                String PopularTeamPost = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[2]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a/small";

                crawling crawled_data = new crawling();
                crawled_data.setTitle(GetString(PopularTeamPost));
                String PopularTeamPostWriter = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[2]/article/div/ul/li["
                        + i + "]/article/div[2]/small";
                ;
                crawled_data.setAuthor(GetString(PopularTeamPostWriter));
                /*
                 * href 가져오기 WebElement link = driver.findElement(By.tagName("a")) String url =
                 * link.getAttribute("href")
                 */
                // *[@id="app"]/div/div/article/div/div[2]/div/div[1]/div[2]/div[1]/article/div/ul/li[14]/article/div[1]/div/span/a

                String linkstring = "//*[@id='app']/div/div/article/div/div[2]/div/div[1]/div[2]/div[2]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a";
                WebElement link = driver.findElement(By.xpath(linkstring));
                crawled_data.setURL(link.getAttribute("href"));
                crawlinglist.add(crawled_data);
            } catch (Exception e) {
                break;
            }
        }
        Wrapper.setCrawlingList(crawlinglist);
        return Wrapper;
    }

    public crawlingWrapper fourCrawling() {
        String PopularTeamlBlogHeaderTitle = GetString(
                "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[2]/article/a");

        crawlingWrapper Wrapper = new crawlingWrapper();
        Wrapper.setHeader(PopularTeamlBlogHeaderTitle);
        System.out.println(PopularTeamlBlogHeaderTitle + " 크롤링 시작");
        List<crawling> crawlinglist = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            try {
                crawling crawled_data = new crawling();
                String PopularTeamPost = " //*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[2]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a/small";
                String PopularTeamPostWriter = "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[2]/article/div/ul/li["
                        + i + "]/article/div[2]/small";
                String linkstring = "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[2]/article/div/ul/li[" + i
                        + "]/article/div[1]/div/span/a";
                crawled_data.setTitle(GetString(PopularTeamPost));
                crawled_data.setAuthor(GetString(PopularTeamPostWriter));
                WebElement link = driver.findElement(By.xpath(linkstring));
                crawled_data.setURL(link.getAttribute("href"));

                crawlinglist.add(crawled_data);
            } catch (Exception e) {
                break;
            }
        }
        Wrapper.setCrawlingList(crawlinglist);
        return Wrapper;
    }

    public crawlingWrapper lastCrawling() {
        String PopularTeamlBlogHeaderTitle = GetString(
                "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[3]/article/a");

        crawlingWrapper Wrapper = new crawlingWrapper();
        Wrapper.setHeader(PopularTeamlBlogHeaderTitle);
        System.out.println(PopularTeamlBlogHeaderTitle + " 크롤링 시작");
        List<crawling> crawlinglist = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            try {
                crawling crawled_data = new crawling();
                String PopularTeamPost = "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[3]/article/div/ul/li["
                        + i + "]/article/div[1]/div/span/a/small";
                String PopularTeamPostWriter = " //*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[3]/article/div/ul/li["
                        + i + "]/article/div[2]/small";
                String linkstring = "//*[@id='app']/div/div/article/div/div[2]/div/div[2]/div[3]/article/div/ul/li[" + i
                        + "]/article/div[1]/div/span/a";

                crawled_data.setTitle(GetString(PopularTeamPost));
                crawled_data.setAuthor(GetString(PopularTeamPostWriter));
                WebElement link = driver.findElement(By.xpath(linkstring));
                crawled_data.setURL(link.getAttribute("href"));

                crawlinglist.add(crawled_data);
            } catch (Exception e) {
                break;
            }
        }
        Wrapper.setCrawlingList(crawlinglist);
        return Wrapper;
    }
}