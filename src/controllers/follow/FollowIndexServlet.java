package controllers.follow;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowIndexServlet
 */
@WebServlet("/follows/index")
public class FollowIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<Report> follows = em.createNamedQuery("InnerFollows", Report.class)
                                        .setParameter("employee", login_employee)
                                        .setFirstResult(15 * (page - 1))
                                        .setMaxResults(15)
                                        .getResultList();
        long follows_count = (long)em.createNamedQuery("InnerFollowsCount", Long.class)
                                        .setParameter("employee", login_employee)
                                        .getSingleResult();

        em.close();


        request.setAttribute("follows", follows);
        request.setAttribute("follows_count", follows_count);
        request.setAttribute("page", page);


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/index.jsp");
        rd.forward(request, response);
    }

}
