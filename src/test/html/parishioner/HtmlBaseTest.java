package parishioner;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class HtmlBaseTest extends TestCase {

	public static final WebClient webClient = new WebClient(BrowserVersion.CHROME_16);
	
	public static final String baseURI = "http://localhost:9999/elim";
	
	@Before
	public void setUp() {
		try {
			webClient.setThrowExceptionOnScriptError(false);
			HtmlPage page = (HtmlPage) webClient.getPage(baseURI + "/auth/login");
			final HtmlForm form = (HtmlForm) page.getElementById("login-form");
			((HtmlTextInput)page.getElementByName("j_username")).setValueAttribute("guest");
			((HtmlPasswordInput)page.getElementByName("j_password")).setValueAttribute("frunzisului25");
			((HtmlSubmitInput)form.getInputByValue("Login")).click();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@After
	public void tearDown() {
		try {
			webClient.getPage(baseURI + "/auth/logout");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
