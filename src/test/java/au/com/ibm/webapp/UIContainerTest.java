package au.com.ibm.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;;

public class UIContainerTest extends ContainerTests {


	@Drone
	protected WebDriver browser;
	
	@ArquillianResource
	protected URL deploymentUrl;
	
	@FindBy
	private WebElement growl;
	
	@FindBy
	private WebElement Signings;
	
	@FindBy
	private WebElement loginLink;
	
	@FindBy
	private WebElement loggedinLink;
	
	@FindBy
	private WebElement mainContent;
	
	@FindBy(id = "form:loginPanelSignInButton")
	private WebElement loginPanelSignInButton;

	@FindBy(id="OpportunityList")
	private WebElement opportunityList;
	
	@FindBy(id = "form:number")
	private WebElement opportunityNo;



	/**
	 * WebDriver based test for testing the xhtml code in browser context
	 * @throws InterruptedException 
	 */
	//@Test
	@RunAsClient
	public void should_run_as_client(@InitialPage LoginPage loginPage) throws InterruptedException {
		String url = deploymentUrl.toExternalForm() + "index.xhtml";
		loginPage.login(browser, url, "admin", "password");
	
		/*
		 * Verify that the user is logged in
		 */
		assertEquals("Signings Actuals vs Targets", Signings.getText());
		assertEquals(true, loggedinLink.isDisplayed());
		
		//Click opportunity
		guardAjax(browser.findElement(By.xpath("//li[contains(@id,'rm_opportunity')]/a"))).click();
		browser.get(url);
		assertEquals(true, opportunityList.isDisplayed());
		
		//Upload file
		String fileName = this.getClass().getResource("pipeline-small.xlsx").getFile();
		browser.findElement(By.xpath("//span[contains(@class,'ui-fileupload-choose')]/input[@type='file']")).sendKeys(fileName);		
		guardAjax(browser.findElement(By.xpath("//div[contains(@class,'ui-fileupload-buttonbar')]/button[contains(@class,'ui-fileupload-upload')]"))).click();
		waitAjax(browser).until().element(By.id("form2:on1")).is().visible();
		
		// Search for Telstra opportunity
		browser.findElement(By.id("form2:on1")).sendKeys("Tel");
		guardAjax(browser.findElement(By.id("form2:advSearch"))).click();
		waitAjax(browser).until().element(By.xpath("//tr[@data-ri='0']/td[@class='ui-column-p-1']/button[@alt='View details']")).is().visible();
		String opportinityValue = browser.findElement(By.xpath("//tr[@data-ri='0']/td[@class='ui-column-p-1']")).getText();
		
		//Verify the opportunity number
		assertEquals(true, opportinityValue.contains("QL-IEUB6TY"));
		
	}


}
