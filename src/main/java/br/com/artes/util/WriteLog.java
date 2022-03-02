//package br.com.artes.util;
//
//import java.io.FileWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import br.com.java.pagseguro.entity.Users;
//
//public class WriteLog {
//
//	
//	 private FileWriter writer;
//	 
//	  public void  register(Users users) throws Exception{
//		  users.setNameUser(users.getNameUser().replaceAll("{}<>+=\'",""));
//		  writer = new FileWriter("c:/seguro/registro/register"+ 
//	         new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+".log", false);
//	      	  
//		  
//		  
//	  }
//}
