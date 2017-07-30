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
 * ���ܣ��ϴ��ļ�
 */
@MultipartConfig //ע��Ҫ���ϸ�ע�⣬������������Ϊ��MultiPart���ϴ�Э��
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
		
		//Ŀ��:��֪�����ļ���˭�ϴ���
		String name = request.getParameter("name");
		
		//������Ҫ��ȡ�����Ӧ��Partʵ��(Ȼ������part��һЩAPI���ļ����в���)
	    Part part = request.getPart("filename");
	    PrintWriter outPrintWriter = response.getWriter();
	    outPrintWriter.print("name="+name+"\n");
	    outPrintWriter.print("ContentType="+part.getContentType()+"\n");
	    outPrintWriter.print("size="+part.getSize()+"\n");
	    //���ｫͼƬ(�ļ�)�����ָ�д���ˣ���ʵ����һ�������ķ�����Ӧ�ö�̬ȥ�����ļ������ֵģ�
	    part.write("C:\\Users\\asus\\Desktop\\avatar.png");
	}

}
