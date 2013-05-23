import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
"classpath:META-INF/spring/spring-security.xml"})
public class Logintest extends TestCase {

	public static final String baseURI = "http://localhost:9999/elim";

	public static final WebClient webClient = new WebClient(BrowserVersion.CHROME_16);

	@Test
	public void testNoAuthentication() throws Exception {

		try {
			System.out.println("Testing No Authentication");
			webClient.setThrowExceptionOnScriptError(false);
			webClient.getPage(baseURI + "/parishioners/list");
		} catch (Exception e) {
			assertTrue(e instanceof AccessDeniedException);
			assertTrue(e.getMessage().contains("Access is denied")); 
		}
	}

	@Test
	public void testFailedAuthentication() throws Exception {

		System.out.println("Testing Successful Authentication");
		webClient.setThrowExceptionOnScriptError(false);
		final HtmlPage page = (HtmlPage) webClient.getPage(baseURI + "/auth/login");
		final HtmlForm form = (HtmlForm) page.getElementById("login-form");
		((HtmlTextInput)page.getElementByName("j_username")).setValueAttribute("invalid");
		((HtmlPasswordInput)page.getElementByName("j_password")).setValueAttribute("invalid");
		final HtmlPage error = ((HtmlSubmitInput)form.getInputByValue("Login")).click();
		assertTrue(error.getUrl().toString().endsWith("error=true"));
		assertTrue(error.getElementById("login-error") != null);
		assertTrue((error.getElementById("login-error")).getTextContent().contains("invalid username or password!"));
		webClient.closeAllWindows();
	}
	
	@Test
	public void testSuccessfulAuthentication() throws Exception {

		System.out.println("Testing Successful Authentication");
		webClient.setThrowExceptionOnScriptError(false);
		final HtmlPage page = (HtmlPage) webClient.getPage(baseURI + "/auth/login");
		final HtmlForm form = (HtmlForm) page.getElementById("login-form");
		((HtmlTextInput)page.getElementByName("j_username")).setValueAttribute("guest");
		((HtmlPasswordInput)page.getElementByName("j_password")).setValueAttribute("frunzisului25");
		final HtmlPage home = ((HtmlSubmitInput)form.getInputByValue("Login")).click();
		assertTrue(home.getUrl().toString().endsWith("elim/"));
		assertTrue(home.getElementById("login-error") == null);
		webClient.closeAllWindows();
	}

}
