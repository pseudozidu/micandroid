package team1.videoplay.category.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import team1.videoplay.category.service.VideoTypeService;
import team1.videoplay.category.service.impl.VideoTypeServiceImpl;
import team1.videoplay.utils.FenYe;

public class SearchTypeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int page = Integer.parseInt(request.getParameter("page"));
		String keyword = request.getParameter("keyword");
		VideoTypeService videoTypeService = VideoTypeServiceImpl.getInstance();
		FenYe allType = videoTypeService.searchVideoTypePageByKeyWord(page, keyword);
		request.setAttribute("allType", allType);
		request.setAttribute("flag", 1);
		request.setAttribute("keyword", keyword);
		request.getRequestDispatcher("manage/typeManage.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
