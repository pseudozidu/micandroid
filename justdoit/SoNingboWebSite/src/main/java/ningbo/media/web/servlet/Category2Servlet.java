package ningbo.media.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ningbo.media.web.api.CategoryAPI;

import org.json.JSONObject;

public class Category2Servlet extends HttpServlet {

	private static final long serialVersionUID = -3168481919592988082L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("utf-8");
			String category1_id = request.getParameter("category1_id");
			CategoryAPI api = new CategoryAPI();
			String res = api.showCategory2(category1_id);
			JSONObject json = new JSONObject(res) ;
			PrintWriter writer = response.getWriter();
	        writer.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

}
