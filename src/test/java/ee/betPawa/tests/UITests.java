package ee.betPawa.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ee.betPawa.locators.Locators.*;
import static ee.betPawa.util.Util.*;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UITests {

    private String LOGIN_PAGE_URL = "https://ug.staging.fe.verekuu.com/";

    private String events = "[class='tabs-content'] [data-test-id='bpEvent']";
    private String eventBets = " [class='event-bets']";
    private String eventBet = " [class='event-bet']";

    public WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_91.0.4472.101\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(LOGIN_PAGE_URL);
        loginToAccount("778899001", "123456", driver);
        setWindowSideAndPosition(driver);
    }

    @AfterEach
    public void closeWindow() {
        driver.quit();
    }

    @Test
    public void someSelections() {
        makeSelections(2);
    }

    @Test
    public void fiveSelections() {
        makeSelections(5);
    }

    @Test
    public void deposit() {
        String initialBalance = balance(driver);

        clickDepositButton(driver);
        clickMtnButton(driver);
        fillDepositAmountField("2000000", driver);
        clickSendDepositButton(driver);
        WebElement transactionProgress = driver.findElement(transactionInProgress);
        try {
            new WebDriverWait(driver, 40)
                    .until(we -> !transactionProgress.isEnabled());
        } catch (StaleElementReferenceException ignored) {

        }

        WebElement webElement = driver.findElement(balance);
        String balanceAfterDeposit = new BigDecimal(initialBalance).add(BigDecimal.valueOf(2000000)).toPlainString();
        new WebDriverWait(driver, 10)
                .withMessage("Balance not Updated After Place Bet")
                .until(we -> webElement.getText().replaceAll("[^0-9.]", "").equalsIgnoreCase(balanceAfterDeposit));

        openStatement(driver);

        assertEquals(balanceAfterDeposit, lastBalanceInStatement(driver).replaceAll("[^0-9.]", ""));
        assertEquals("+2 000 000.00", lastChangeBalanceInStatement(driver));
    }

    private void makeSelections(int selections) {
        String initialBalance = balance(driver);
        String stake = "1";
        openUpcomingEvents(driver);

        List<WebElement> eventsList = driver.findElements(By.cssSelector(events));
        ArrayList<Integer> listOfEventNumbers = new ArrayList<>();
        for (int k = 1; k <= eventsList.size(); k++) {
            listOfEventNumbers.add(k);
        }

        for (int i = 1; i <= selections; i++) {
            int min = 1;
            int eventNumber = listOfEventNumbers.get((int) Math.floor(Math.random() * listOfEventNumbers.size()));
            listOfEventNumbers.remove(listOfEventNumbers.indexOf(eventNumber));
            String eventLocator = events + ":nth-child(" + eventNumber + ")";
            int maximum = 3;
            maximum -= min;
            int oddNumber = min + (int)(Math.random()*((maximum - min) + 1));
            WebElement oddOfEvent = driver.findElement(By.cssSelector(eventLocator + eventBets + eventBet + ":nth-child(" + oddNumber + ")"));
            oddOfEvent.click();
            if (i == 1) {
                clickAcceptOddsChangeCheckbox(driver);
            }
        }
        enterStake(stake, driver);

        String betSlipOdd = betSlipOddsValue(driver);
        String betSlipPotentialWinning = betSlipPotentialWinningValue(driver);
        String betSlipPayout = betSlipPayoutValue(selections, driver);
        BigDecimal betSlipBonus = ZERO;
        if (selections > 2) {
            betSlipBonus = new BigDecimal(betSlipBonusValue(driver));
        }
        clickPlaceBetButton(driver);

        WebElement webElement = driver.findElement(balance);
        String expectedBalanceAfterBets = new BigDecimal(initialBalance).subtract(new BigDecimal(stake)).toPlainString();
        new WebDriverWait(driver, 10)
                .withMessage("Balance not Updated After Place Bet")
                .until(we -> webElement.getText().replaceAll("[^0-9.]", "").equalsIgnoreCase(expectedBalanceAfterBets));

        String lastBetNumber = findLastBetNumber(driver);
        openStatement(driver);
        String lastBetNumberInStatement = checkLastBetInStatement(lastBetNumber, driver);
        assertEquals("Bet #"+ lastBetNumber + " Placed", lastBetNumberInStatement, "Last Bet is not Present in statement");
        driver.findElement(lastActionInStatement).click();

        openMyBets(driver);
        driver.findElement(By.cssSelector("[data-test-id='bet-open-" + lastBetNumber + "']")).click();
        assertEquals(stake + ".00", myBetsStakeAmount(driver), "Incorrect Stake Amount Between Bet Slip and My Bets");
        assertEquals(betSlipOdd, myBetsOddsAmount(driver), "Incorrect Odd Amount Between Bet Slip and My Bets");
        assertEquals(betSlipPotentialWinning, myBetsPotentialWinningAmount(driver), "Incorrect Potential Winning Amount Between Bet Slip and My Bets");
        assertEquals(betSlipPayout, myBetsPayoutAmount(driver), "Incorrect Potential Winning Amount Between Bet Slip and My Bets");
        if (selections > 2) {
            BigDecimal potentialWinningAmount = new BigDecimal(myBetsPotentialWinningAmount(driver));
            BigDecimal winBonusAmount = new BigDecimal(myBetsWinBonusAmount(driver));
            assertEquals(potentialWinningAmount.divide(TEN).setScale(2, RoundingMode.DOWN), winBonusAmount, "Incorrect Bonus Amount Calculation");
            assertEquals(betSlipBonus, winBonusAmount, "Incorrect Bonus Amount Between Bet Slip and My Bets");
        }
    }
}
