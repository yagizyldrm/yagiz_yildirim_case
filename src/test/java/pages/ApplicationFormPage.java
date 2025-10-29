package pages;

import org.openqa.selenium.By;

public class ApplicationFormPage extends BasePage {
    public By nameInput = By.xpath("//input[@data-qa='name-input']");
    public By emailInput = By.xpath("//input[@data-qa='email-input']");
    public By linkedinInput = By.xpath("//input[@name='urls[LinkedIn]']");
    public By coverLetterTextArea = By.xpath("//textarea[@name='comments']");
    public By submitButton = By.xpath("//button[@id='btn-submit']");
}
