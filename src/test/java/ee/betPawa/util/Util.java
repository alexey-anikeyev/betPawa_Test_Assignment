package ee.betPawa.util;

import ee.betPawa.locators.Locators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;


import static ee.betPawa.locators.Locators.*;

public class Util {

    public static void loginToAccount(String mobileNumber, String password, WebDriver driver) {
        driver.findElement(loginButtonOnMainPage).click();
        driver.findElement(mobileNumberField).sendKeys(mobileNumber);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButtonOnLoginPage).click();

        WebElement webElement = driver.findElement(balanceTitle);
        new WebDriverWait(driver, 10)
                .until(we -> webElement.isDisplayed());
    }

    public static void setWindowSideAndPosition(WebDriver driver) {
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1600, 900));
    }

    public static void clickAcceptOddsChangeCheckbox(WebDriver driver) {
        driver.findElement(acceptOddsChangeCheckbox).click();
    }

    public static void clickDepositButton(WebDriver driver) {
        driver.findElement(depositButton).click();
    }

    public static void clickPlaceBetButton(WebDriver driver) {
        driver.findElement(placeBetButton).click();
    }

    public static void clickMtnButton(WebDriver driver) {
        driver.findElement(mTn).click();
    }

    public static void clickSendDepositButton(WebDriver driver) {
        driver.findElement(sendDepositButton).click();
    }

    public static void enterStake(String stakeAmount, WebDriver driver) {
        driver.findElement(stakeInputField).sendKeys(stakeAmount);
    }

    public static void fillDepositAmountField(String amount, WebDriver driver) {
        driver.findElement(depositAmountField).sendKeys(amount);
    }

    public static void openMyBets(WebDriver driver) {
        driver.findElement(menuButton).click();
        driver.findElement(myBetsMenuButton).click();
    }

    public static void openStatement(WebDriver driver) {
        driver.findElement(menuButton).click();
        driver.findElement(statementsMenuButton).click();
    }

    public static void openUpcomingEvents(WebDriver driver) {
        driver.findElement(upcomingEvents).click();
    }

    public static String checkLastBetInStatement(String lastBetId, WebDriver driver) {
        return driver.findElement(By.xpath("//a[@href='/betslip/"  + lastBetId + "']")).getText();
    }

    public static String findLastBetNumber(WebDriver driver) {
        driver.findElement(checkBetDetails).click();
        return driver.findElement(Locators.betId).getText().replaceAll("[^0-9.]", "");
    }

    public static String lastBalanceInStatement(WebDriver driver) {
        return driver.findElement(lastBalanceInStatement).getText();
    }

    public static String lastChangeBalanceInStatement(WebDriver driver) {
        return driver.findElement(lastChangeBalanceInStatement).getText();
    }

    public static String balance(WebDriver driver) {
        return driver.findElement(balance).getText().replaceAll("[^0-9.]", "");
    }

    public static String myBetsOddsAmount(WebDriver driver) {
        return driver.findElement(oddsAmount).getText().replaceAll("[^0-9.]", "");
    }

    public static String myBetsPayoutAmount(WebDriver driver) {
        return driver.findElement(payoutAmount).getText().replaceAll("[^0-9.]", "");
    }

    public static String myBetsPotentialWinningAmount(WebDriver driver) {
        return driver.findElement(potentialWinningAmount).getText().replaceAll("[^0-9.]", "");
    }

    public static String myBetsStakeAmount(WebDriver driver) {
        return driver.findElement(stakeAmount).getText().replaceAll("[^0-9.]", "");
    }

    public static String myBetsWinBonusAmount(WebDriver driver) {
        return driver.findElement(winBonusAmount).getText().replaceAll("[^0-9.]", "");
    }

    public static String betSlipOddsValue(WebDriver driver) {
        return betSlipElementValue(1, driver);
    }

    public static String betSlipPotentialWinningValue(WebDriver driver) {
        return betSlipElementValue(2, driver);
    }

    public static String betSlipBonusValue(WebDriver driver) {
        return betSlipElementValue(3, driver);
    }

    public static String betSlipPayoutValue(int selectionsNumber, WebDriver driver) {
        return betSlipElementValue(selectionsNumber > 2 ? 4 : 3, driver);
    }

    private static String betSlipElementValue(int selectionsNumber, WebDriver driver) {
        return driver.findElement(By.cssSelector("[class = 'bet-details'] div:nth-child(" + selectionsNumber + ") [class = 'right']")).getText().replaceAll("[^0-9.]", "");
    }
}
