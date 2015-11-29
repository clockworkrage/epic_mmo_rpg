package frontend;

import dbservice.DBService;
import main.AccountService;
import main.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by uschsh on 02.11.15.
 */
public class AdminServletTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private DBService dbService;
    private AccountService accountService;

    @Before
    public void setUp() throws IOException {
        dbService = new DBService();
        accountService = new AccountService(dbService);
        accountService.addUser("firstUser", new UserProfile("firstUser", "password", "email"));
        accountService.addUser("secondUser", new UserProfile("secondUser", "password", "email"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDoGet() throws IOException {
        AdminServlet adminServlet = new AdminServlet(accountService);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        //assertEquals(accountService.getAuthUsersNumber(), 0);
        assertEquals(accountService.getRegUsersNumber(), 2);
        dbService.shutdown();
    }

    @Test
    public void testDoGetStopServer() throws IOException {
        //when(request.getParameter("shutdown")).thenReturn("100");

        AdminServlet adminServlet = new AdminServlet(accountService);

        try {
            adminServlet.doGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        dbService.shutdown();
    }
}