package ee.betPawa.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;


import static ee.betPawa.locators.Locators.*;

public class Util {

    public static WebDriver driver;

    public static void loginToAccount(String mobileNumber, String password) {
        click(loginButtonOnMainPage);
        fillTheField(mobileNumberField, mobileNumber);
        fillTheField(passwordField, password);
        click(loginButtonOnLoginPage);

        WebElement webElement = driver.findElement(balanceTitle);
        new WebDriverWait(driver, 10)
                .until(we -> webElement.isDisplayed());
    }

    public static void setWindowSideAndPosition() {
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().setSize(new Dimension(1600, 900));
    }

    public static void clickAcceptOddsChangeCheckbox() {
        click(acceptOddsChangeCheckbox);
    }

    public static void clickDepositButton() {
        click(depositButton);
    }

    public static void clickPlaceBetButton() {
        click(placeBetButton);
    }

    public static void clickMtnButton() {
        click(mTn);
    }

    public static void clickSendDepositButton() {
        click(sendDepositButton);
    }

    public static void enterStake(String stakeAmount) {
        fillTheField(stakeInputField, stakeAmount);
    }

    public static void fillDepositAmountField(String amount) {
        fillTheField(depositAmountField, amount);
    }

    public static void openMyBets() {
        click(menuButton);
        click(myBetsMenuButton);
    }

    public static void openStatement() {
        click(menuButton);
        click(statementsMenuButton);
    }

    public static void openUpcomingEvents() {
        click(upcomingEvents);
    }

    public static String checkLastBetInStatement(String lastBetId) {
        return driver.findElement(By.xpath("//a[@href='/betslip/"  + lastBetId + "']")).getText();
    }

    public static String findLastBetNumber() {
        click(checkBetDetails);
        return textValue(betId);
    }

    public static String lastBalanceInStatement() {
        return textValue(lastBalanceInStatement);
    }

    public static String lastChangeBalanceInStatement() {
        return textValue(lastChangeBalanceInStatement);
    }

    public static String balance() {
        return textValue(balance);
    }

    public static String myBetsOddsAmount() {
        return textValue(oddsAmount);
    }

    public static String myBetsPayoutAmount() {
        return textValue(payoutAmount);
    }

    public static String myBetsPotentialWinningAmount() {
        return textValue(potentialWinningAmount);
    }

    public static String myBetsStakeAmount() {
        return textValue(stakeAmount);
    }

    public static String myBetsWinBonusAmount() {
        return textValue(winBonusAmount);
    }

    public static String betSlipOddsValue() {
        return betSlipElementValue(1);
    }

    public static String betSlipPotentialWinningValue() {
        return betSlipElementValue(2);
    }

    public static String betSlipBonusValue() {
        return betSlipElementValue(3);
    }

    public static String betSlipPayoutValue(int selectionsNumber) {
        return betSlipElementValue(selectionsNumber > 2 ? 4 : 3);
    }

    public static void click(By locator) {
        driver.findElement(locator).click();
    }

    public static void fillTheField(By locator, String fieldValue) {
        driver.findElement(locator).sendKeys(fieldValue);
    }

    private static String betSlipElementValue(int selectionsNumber) {
        return driver.findElement(By.cssSelector("[class = 'bet-details'] div:nth-child(" + selectionsNumber + ") [class = 'right']")).getText().replaceAll("[^0-9.]", "");
    }

    private static String textValue(By locator) {
        return driver.findElement(locator).getText().replaceAll("[^0-9.]", "");
    }
}
