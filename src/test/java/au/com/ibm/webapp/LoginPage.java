package au.com.ibm.webapp;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.page.Location;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("index.xhtml")
public class LoginPage {

    @FindBy
    private WebElement userName;

    @FindBy
    private WebElement password;

    @FindBy(id = "login")
    private WebElement loginButton;
    
	@FindBy
	private WebElement loginLink;
	
	@FindBy
	private WebElement loggedinLink;
	
	@FindBy(id = "form:loginPanelSignInButton")
	private WebElement loginPanelSignInButton;
	

	@FindBy
	private WebElement Signings;
    
    public void login(WebDriver browser,String url, String userName, String password) {
    
    	 browser.get(url);	
 		
 		/*
 		 * Find the login button and click it
 		 */
 		System.out.println("Opening Login page and signing in...");
 		assertNotNull(loginLink);
 		guardAjax(loginLink).click();
 		
 		/*
 		 * Test that login page is displayed
 		 */
 		// TODO how to avoid page refresh
 		browser.get(url);
 		/*
 		 * Fill in the form and click sign in
 		 */
 		browser.findElement(By.id("form:loginPanelUser")).sendKeys(userName);
 		browser.findElement(By.id("form:loginPanelPassword")).sendKeys(password);
 		guardAjax(loginPanelSignInButton).click();		
 		
 		/*
 		 * Verify that the user is logged in
 		 */
 		waitModel().until().element(Signings).is().present(); 
 	
	
        
    }

    
}