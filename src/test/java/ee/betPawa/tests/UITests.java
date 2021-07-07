package ee.betPawa.tests;

import ee.betPawa.util.Util;
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
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UITests extends Util {

    private String LOGIN_PAGE_URL = "https://ug.staging.fe.verekuu.com/";

    private String events = "[class='tabs-content'] [data-test-id='bpEvent']";
    private String eventBets = " [class='event-bets']";
    private String eventBet = " [class='event-bet']";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_91.0.4472.101\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(LOGIN_PAGE_URL);
        loginToAccount("778899001", "123456");
        setWindowSideAndPosition();
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
        String initialBalance = balance();

        clickDepositButton();
        clickMtnButton();
        fillDepositAmountField("2000000");
        clickSendDepositButton();
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

        openStatement();

        assertEquals(balanceAfterDeposit, lastBalanceInStatement().replaceAll("[^0-9.]", ""));
        assertEquals("+2000000.00", "+" + lastChangeBalanceInStatement());
    }

    private void makeSelections(int selections) {
        String initialBalance = balance();
        String stake = "1";
        openUpcomingEvents();

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
            By oddOfEvent = By.cssSelector(eventLocator + eventBets + eventBet + ":nth-child(" + oddNumber + ")");
            click(oddOfEvent);
            if (i == 1) {
                clickAcceptOddsChangeCheckbox();
            }
        }
        enterStake(stake);

        String betSlipOdd = betSlipOddsValue();
        String betSlipPotentialWinning = betSlipPotentialWinningValue();
        String betSlipPayout = betSlipPayoutValue(selections);
        BigDecimal betSlipBonus = ZERO;
        if (selections > 2) {
            betSlipBonus = new BigDecimal(betSlipBonusValue());
        }
        clickPlaceBetButton();

        WebElement webElement = driver.findElement(balance);
        String expectedBalanceAfterBets = new BigDecimal(initialBalance).subtract(new BigDecimal(stake)).toPlainString();
        new WebDriverWait(driver, 10)
                .withMessage("Balance not Updated After Place Bet")
                .until(we -> webElement.getText().replaceAll("[^0-9.]", "").equalsIgnoreCase(expectedBalanceAfterBets));

        String lastBetNumber = findLastBetNumber();
        openStatement();
        String lastBetNumberInStatement = checkLastBetInStatement(lastBetNumber);
        assertEquals("Bet #"+ lastBetNumber + " Placed", lastBetNumberInStatement, "Last Bet is not Present in statement");
        click(lastActionInStatement);

        openMyBets();
        click(By.cssSelector("[data-test-id='bet-open-" + lastBetNumber + "']"));
        assertEquals(stake + ".00", myBetsStakeAmount(), "Incorrect Stake Amount Between Bet Slip and My Bets");
        assertEquals(betSlipOdd, myBetsOddsAmount(), "Incorrect Odd Amount Between Bet Slip and My Bets");
        assertEquals(betSlipPotentialWinning, myBetsPotentialWinningAmount(), "Incorrect Potential Winning Amount Between Bet Slip and My Bets");
        assertEquals(betSlipPayout, myBetsPayoutAmount(), "Incorrect Potential Winning Amount Between Bet Slip and My Bets");
        if (selections > 2) {
            BigDecimal potentialWinningAmount = new BigDecimal(myBetsPotentialWinningAmount());
            BigDecimal winBonusAmount = new BigDecimal(myBetsWinBonusAmount());
            assertEquals(potentialWinningAmount.divide(TEN).setScale(2, RoundingMode.DOWN), winBonusAmount, "Incorrect Bonus Amount Calculation");
            assertEquals(betSlipBonus, winBonusAmount, "Incorrect Bonus Amount Between Bet Slip and My Bets");
        }
    }
}
