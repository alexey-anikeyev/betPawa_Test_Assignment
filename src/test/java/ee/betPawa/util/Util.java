package ee.betPawa.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;


import static ee.betPawa.locators.Locators.*;

public class Util {

    public static void loginToAccount(String mobileNumber, String password, WebDriver driver) {
        click(loginButtonOnMainPage, driver);
        fillTheField(mobileNumberField, mobileNumber, driver);
        fillTheField(passwordField, password, driver);
        click(loginButtonOnLoginPage, driver);

        WebElement webElement = driver.findElement(balanceTitle);
        new WebDriverWait(driver, 10)
                .until(we -> webElement.isDisplayed());
    }

    public static void setWindowSideAndPosition(WebDriver driver) {
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1600, 900));
    }

    public static void clickAcceptOddsChangeCheckbox(WebDriver driver) {
        click(acceptOddsChangeCheckbox, driver);
    }

    public static void clickDepositButton(WebDriver driver) {
        click(depositButton, driver);
    }

    public static void clickPlaceBetButton(WebDriver driver) {
        click(placeBetButton, driver);
    }

    public static void clickMtnButton(WebDriver driver) {
        click(mTn, driver);
    }

    public static void clickSendDepositButton(WebDriver driver) {
        click(sendDepositButton, driver);
    }

    public static void enterStake(String stakeAmount, WebDriver driver) {
        fillTheField(stakeInputField, stakeAmount, driver);
    }

    public static void fillDepositAmountField(String amount, WebDriver driver) {
        fillTheField(depositAmountField, amount, driver);
    }

    public static void openMyBets(WebDriver driver) {
        click(menuButton, driver);
        click(myBetsMenuButton, driver);
    }

    public static void openStatement(WebDriver driver) {
        click(menuButton, driver);
        click(statementsMenuButton, driver);
    }

    public static void openUpcomingEvents(WebDriver driver) {
        click(upcomingEvents, driver);
    }

    public static String checkLastBetInStatement(String lastBetId, WebDriver driver) {
        return driver.findElement(By.xpath("//a[@href='/betslip/"  + lastBetId + "']")).getText();
    }

    public static String findLastBetNumber(WebDriver driver) {
        click(checkBetDetails, driver);
        return textValue(betId, driver);
    }

    public static String lastBalanceInStatement(WebDriver driver) {
        return textValue(lastBalanceInStatement, driver);
    }

    public static String lastChangeBalanceInStatement(WebDriver driver) {
        return textValue(lastChangeBalanceInStatement, driver);
    }

    public static String balance(WebDriver driver) {
        return textValue(balance, driver);
    }

    public static String myBetsOddsAmount(WebDriver driver) {
        return textValue(oddsAmount, driver);
    }

    public static String myBetsPayoutAmount(WebDriver driver) {
        return textValue(payoutAmount, driver);
    }

    public static String myBetsPotentialWinningAmount(WebDriver driver) {
        return textValue(potentialWinningAmount, driver);
    }

    public static String myBetsStakeAmount(WebDriver driver) {
        return textValue(stakeAmount, driver);
    }

    public static String myBetsWinBonusAmount(WebDriver driver) {
        return textValue(winBonusAmount, driver);
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

    public static void click(By locator, WebDriver driver) {
        driver.findElement(locator).click();
    }

    public static void fillTheField(By locator, String fieldValue, WebDriver driver) {
        driver.findElement(locator).sendKeys(fieldValue);
    }

    private static String betSlipElementValue(int selectionsNumber, WebDriver driver) {
        return driver.findElement(By.cssSelector("[class = 'bet-details'] div:nth-child(" + selectionsNumber + ") [class = 'right']")).getText().replaceAll("[^0-9.]", "");
    }

    private static String textValue(By locator, WebDriver driver) {
        return driver.findElement(locator).getText().replaceAll("[^0-9.]", "");
    }
}
