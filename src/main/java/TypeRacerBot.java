import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.ThreadLocalRandom;


public class TypeRacerBot {

    private static final String ACCEPT_COOKIES_XPATH = "/html/body/div[1]/div/div/div[2]/button[2]";
    private static final String JOIN_RACE_XPATH = "//*[@id=\"gwt-uid-17\"]/table/tbody/tr[3]/td/table/tbody/tr/td[2]";
    private static final String TEXT_TO_TYPE_XPATH = "/html/body/div[1]/div/div[1]/div[1]/table/tbody/tr[2]/td[2]/div/div[1]/table/tbody/tr[3]/td/div/div/table/tbody/tr[2]/td[3]/table/tbody/tr[2]/td/table/tbody/tr[1]/td/table/tbody/tr[1]/td/div/div";
    private static final String BEGIN_TEST_XPATH = "/html/body/div[9]/div/div/div[2]/div/div/table/tbody/tr[4]/td/button";

    private final WebDriver webDriver = new FirefoxDriver();

    public TypeRacerBot(String url, int typeSpeed) {
        webDriver.get(url);

        while(true) {
            try {
                WebElement acceptCookies = findElement(By.xpath(ACCEPT_COOKIES_XPATH));
                if (acceptCookies != null && acceptCookies.isEnabled()) {
                    try {
                        acceptCookies.click();
                    } catch (Exception ignored) {}
                }

                WebElement joinRace = findElement(By.xpath(JOIN_RACE_XPATH));
                if (joinRace != null && joinRace.isEnabled()) {
                    try {
                        joinRace.click();
                    } catch (Exception ignored) {}
                }

                WebElement input = findElement(By.className("txtInput"));
                String inputValue = input != null ? input.getAttribute("value") : null;

                if (inputValue != null && !inputValue.equals("Type the above text here when the race begins")) {
                    WebElement textElement = findElement(By.xpath(TEXT_TO_TYPE_XPATH));
                    String text = textElement != null ? textElement.getText() : null;

                    if (text != null) {
                        for (String string : text.split("\\s+")) {
                            for (char c : string.toCharArray()) {
                                input.sendKeys(String.valueOf(c));
                                Thread.sleep((int) ThreadLocalRandom.current().nextDouble(typeSpeed, typeSpeed+20));
                            }
                            input.sendKeys(" ");
                        }
                    }
                }
            } catch (Exception ignored) {}

            try {
                Thread.sleep(600);
            } catch (Exception ignored) {}
        }
    }

    public WebElement findElement(By by) {
        try {
            return webDriver.findElement(by);
        } catch (Exception ignored) {}
        return null;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Run example \"java -jar typeracerbot.jar 30 https://play.typeracer.com?rt=ozc5s4g4p\"");
            System.exit(1);
        } else if (args.length == 2) {
            if (System.getProperty("os.name", "").startsWith("Windows")) {
                System.setProperty("webdriver.gecko.driver", "./geckodriver.exe");
            } else {
                System.setProperty("webdriver.gecko.driver", "./geckodriver");
            }
        } else {
            System.setProperty("webdriver.gecko.driver", args[2]);
        }

        new TypeRacerBot(args[1], Integer.parseInt(args[0]));
    }


}
