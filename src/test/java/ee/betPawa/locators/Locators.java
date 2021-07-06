package ee.betPawa.locators;

import org.openqa.selenium.By;

public class Locators {

    public static By balance = By.cssSelector("[class='balance'] [class='count']");
    public static By balanceTitle = By.cssSelector("[class='balance-title']");

    //Login Related
    public static By loginButtonOnMainPage = By.cssSelector("[class='header-buttons'] [class*='button-accent']");
    public static By mobileNumberField = By.cssSelector("[id='login-form-phoneNumber']");
    public static By passwordField = By.cssSelector("[id='login-form-password']");
    public static By loginButtonOnLoginPage = By.cssSelector("[data-test-id='logInButton']");

    //Deposit Related
    public static By sendDepositButton = By.cssSelector("[data-test-id='depositButton']");
    public static By transactionInProgress = By.cssSelector("[data-test-id='tag-PAYMENTCOMPONENT'] [class='notify warning']");

    //Menu Bets Related
    public static By upcomingEvents = By.xpath("//a[@href='/upcoming']");

    //Last Bet Related
    public static By checkBetDetails = By.cssSelector("[class='notify success'] [class='underline']");
    public static By betId = By.cssSelector("[data-test-id='betSlipID']");

    //Menu Related Buttons
    public static By depositButton = By.cssSelector("[class='header-buttons']");
    public static By mTn = By.xpath("//a[@href='/mtn-ug-po']");
    public static By depositAmountField = By.cssSelector("[id='deposit-form-amount-input']");
    public static By menuButton = By.cssSelector("[class='menu-button button']");

    //Placing Bets Related
    public static By acceptOddsChangeCheckbox = By.cssSelector("[class='accept-change'] [type='checkbox']");
    public static By stakeInputField = By.cssSelector("[id='betslip-form-stake-input']");
    public static By placeBetButton = By.cssSelector("[class*='place-bet'] [value='PLACE BET']");

    //Statement
    public static By statementsMenuButton = By.xpath("//a[@href='/statements']");
    public static By lastBalanceInStatement = By.cssSelector("[data-test-class='statementsBody'] tr:nth-child(1) [class='balance']");
    public static By lastChangeBalanceInStatement = By.cssSelector("[data-test-class='statementsBody'] tr:nth-child(1) [class='negative amount'], " +
            "[data-test-class='statementsBody'] tr:nth-child(1) [class='positive amount']");
    public static By lastActionInStatement = By.cssSelector("[data-test-class='statementsBody'] tr:nth-child(1) td a");

    //My Bets
    public static By myBetsMenuButton = By.xpath("//a[@href='/bets/open']");
    public static By oddsAmount = By.cssSelector("[class='summary-line'] [class='value']");
    public static By stakeAmount = By.cssSelector("[data-test-id='stakeAmount'] [class='amount']");
    public static By potentialWinningAmount = By.cssSelector("[data-test-id='possibleWinAmount'] [class='amount']");
    public static By winBonusAmount = By.cssSelector("[data-test-id='winBonusLine'] [class='amount']");
    public static By payoutAmount = By.cssSelector("[data-test-id='betResultPending'] [class='amount']");
}
