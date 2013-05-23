import org.junit.Test;

import parishioner.HtmlBaseTest;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LogoutTest extends HtmlBaseTest {

	public static String LOGIN_HREF = "/elim/auth/login";
	public static String LOGOUT_HREF = "/elim/auth/logout";
	public static String LIST_HREF = "/elim/parishioners/list";
	
	@Test
	public void testLogout () throws Exception {

		HtmlPage page = (HtmlPage) webClient.getPage((HtmlBaseTest.baseURI + "/parishioners/list").toString());
		System.out.println(page.getUrl());
		HtmlAnchor logoutRef = page.getAnchorByHref(LOGOUT_HREF);
		
		assertTrue(logoutRef != null);
		for (HtmlAnchor anchor:page.getAnchors())
			assertFalse(anchor.getHrefAttribute().equalsIgnoreCase(LOGIN_HREF));
		
		page = logoutRef.click();
		HtmlAnchor loginRef = page.getAnchorByHref(LOGIN_HREF);
		
		assertTrue(loginRef != null);
		assertTrue(page.getUrl().toString().compareTo(HtmlBaseTest.baseURI + "/") == 0);
		
		HtmlAnchor listRef = page.getAnchorByHref(LIST_HREF);
		page = listRef.click();
		
		assertTrue(page.getUrl().toString().compareTo(HtmlBaseTest.baseURI + "/auth/login") == 0);
	}

}
