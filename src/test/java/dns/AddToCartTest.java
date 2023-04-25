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

public class AddToCartTest
{
    WebDriver driver;

    @BeforeEach
    public void setup()
    {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 1000));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    /**
     * Тест на добавление в корзину
     */
    @Test
    public void addToCart()
    {
        // зашли на страничку
        driver.get("https://www.dns-shop.ru/product/5429da9af387ed20/61-smartfon-apple-iphone-13-128-gb-cernyj/");

        // добавили товар
        driver.findElement(By.cssSelector(".buy-btn:nth-child(3)")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-link-counter__badge")));

        // перешли в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        // проверили что добавилось успешно
        WebElement element = driver.findElement(By.cssSelector(".cart-link-counter__badge"));
        String textElement = element.getText();
        String message = String.format("В избранном неверное количество товаров. Ожидалось: %s, Получили: %s", "1", textElement);
        Assertions.assertEquals("1", textElement, message);

        // очистили корзину
        driver.findElement(By.cssSelector(".remove-button__title")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Вернуть удалённый товар")));
    }

    /**
     * Тест на удаление из корзины
     */
    @Test
    public void deleteFromCart()
    {
        WebElement element = null;
        // зашли на страничку
        driver.get("https://www.dns-shop.ru/product/5429da9af387ed20/61-smartfon-apple-iphone-13-128-gb-cernyj/");

        // добавили товар
        driver.findElement(By.cssSelector(".buy-btn:nth-child(3)")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-link-counter__badge")));

        // перешли в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        // очистили корзину
        driver.findElement(By.cssSelector(".remove-button__title")).click();

        // проверили что удалилось успешно
        try
        {
            element = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        }
        catch (NoSuchElementException e)
        {
            Assertions.fail("Элемент не найден на странице");
        }

        String textElement = element.getText();
        Assertions.assertEquals("Корзина пуста", textElement);

    }

    @AfterEach
    public void close()
    {
        driver.quit();
    }
}