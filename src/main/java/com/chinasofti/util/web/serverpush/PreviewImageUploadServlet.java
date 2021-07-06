package com.chinasofti.util.web.serverpush;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.util.web.upload.FormFile;
import com.chinasofti.util.web.upload.MultipartRequestParser;

public class PreviewImageUploadServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PreviewImageUploadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MultipartRequestParser parser = new MultipartRequestParser();
		PreviewImageInfo info = (PreviewImageInfo) parser.parse(request,
				"com.chinasofti.util.web.serverpush.PreviewImageInfo");
		FormFile img = info.getUploadFile();
		String path = getServletContext().getRealPath("/images");
		img.saveToFileSystem(request, path + "/" + img.getFileName());

		// 请求中数据的字符集编码是"utf-8",正确设置后防止获取到的数据变成乱码
		request.setCharacterEncoding("utf-8");
		
		// 创建服务器推送数据的消息生产者
		MessageProducer producer = new MessageProducer();
		// 遍历所有的客户端

		// 向每一个客户端生产一个汇率消息
		producer.sendMessage(request.getSession().getId().toString(),
				"upstate", img.getFileName());

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
