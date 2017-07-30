package com.liangjing.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * Servlet implementation class UploadServlet
 * 功能：上传文件
 */
@MultipartConfig //注意要加上该注解，否则它不会认为是MultiPart的上传协议
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath()+"\n");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		//目的:想知道该文件是谁上传的
		String name = request.getParameter("name");
		
		//首先需要获取到相对应的Part实例(然后利用part的一些API对文件进行操作)
	    Part part = request.getPart("filename");
	    PrintWriter outPrintWriter = response.getWriter();
	    outPrintWriter.print("name="+name+"\n");
	    outPrintWriter.print("ContentType="+part.getContentType()+"\n");
	    outPrintWriter.print("size="+part.getSize()+"\n");
	    //这里将图片(文件)的名字给写死了，其实对于一个真正的服务，是应该动态去生成文件的名字的！
	    part.write("C:\\Users\\asus\\Desktop\\avatar.png");
	}

}
