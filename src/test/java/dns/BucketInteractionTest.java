package dns;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BucketInteractionTest
{
    WebDriver webDriver;

    @BeforeEach
    public void setup()
    {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        webDriver = new ChromeDriver();
        webDriver.manage().window().setSize(new Dimension(1280, 720));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Тест на добавление в корзину
     */
    @Test
    public void addToBucket()
    {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));

        // Open site
        webDriver.get("https://www.dns-shop.ru/product/7eb7b07e3949ed20/156-noutbuk-tecno-megabook-t1-seryj/");

        // Add item
        webDriver.findElement(By.cssSelector(".product-card-top__buy")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-link-counter__badge")));

        // Open bucket
        webDriver.get("https://www.dns-shop.ru/cart/");

        // Check if request success
        WebElement element = webDriver.findElement(By.cssSelector(".cart-link-counter__badge"));
        String textElement = element.getText();
        String message = String.format("Except: %s, Got: %s", "1", textElement);
        Assertions.assertEquals("1", textElement, message);
        webDriver.findElement(By.cssSelector(".remove-button__title")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Вернуть удалённый товар")));
    }

    /**
     * Тест на удаление из корзины
     */
    @Test
    public void deleteFromBucket()
    {
        WebElement element = null;
        // Open site
        webDriver.get("https://www.dns-shop.ru/product/7eb7b07e3949ed20/156-noutbuk-tecno-megabook-t1-seryj/");

        // Add item
        webDriver.findElement(By.cssSelector(".buy-btn:nth-child(3)")).click();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-link-counter__badge")));

        // Open bucket
        webDriver.get("https://www.dns-shop.ru/cart/");

        // Clear bucket
        webDriver.findElement(By.cssSelector(".remove-button__title")).click();

        // Check success delete
        try
        {
            element = webDriver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        }
        catch (NoSuchElementException e)
        {
            Assertions.fail("Elemnt not found");
        }

        String textElement = element.getText();
        Assertions.assertEquals("Bucket is empty", textElement);

    }

    @AfterEach
    public void close()
    {
        webDriver.quit();
    }
}