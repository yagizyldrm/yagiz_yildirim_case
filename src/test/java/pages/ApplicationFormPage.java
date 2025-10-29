package pages;

import org.openqa.selenium.By;

public class ApplicationFormPage extends BasePage {
    public By nameInput = By.xpath("//*[@data-qa='name-input']");
    public By emailInput = By.xpath("//*[@data-qa='email-input']");
    public By linkedinInput = By.xpath("//*[@name='urls[LinkedIn]']");
    public By coverLetterTextArea = By.xpath("//*[@name='comments']");
    public By submitButton = By.xpath("//*[@id='btn-submit']");
}
