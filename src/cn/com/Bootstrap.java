package cn.com;

import cn.hutool.core.util.NetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstrap {
    public static void main(String[] args) {

        try {
// 表示本服务器使用的端口号是 18080(tomcat 默认的端口号是8080)
            int port = 18080;
// 获取可用端口 NetUtil.isUsableLocalPort()
// NetUtil.isUsableLocalPort 用来判断端口是否被占用，返回 true 表示没有被占用
            if(!NetUtil.isUsableLocalPort(port)) {
                System.out.println(port +" 此端口已经被占用了，请更改端口号");
                return;
            }
//在端口18080上启动 ServerSocket。

            ServerSocket ss = new ServerSocket(port);
// ----最后一步  关闭客户端对应的 socket
            while(true) {
//服务端和浏览器通信是通过 Socket进行通信的，所以这里需要启动一个 ServerSocket
                Socket s =  ss.accept();
//这表示收到一个浏览器客户端的请求
                InputStream is= s.getInputStream();
//打开输入流，准备接受浏览器提交的信息
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                is.read(buffer);
//准备一个长度是 1024 的字节数组，把浏览器的信息读取出来放进去。 浏览器提交的数据可能短于或者长于1024，所以这种做法是有缺陷的。
                String requestString = new String(buffer,"utf-8");
                System.out.println("浏览器的输入信息： \r\n" + requestString);
//把字节数组转换成字符串，并且打印出来
                OutputStream os = s.getOutputStream();
//打开输出流，准备给客户端输出信息
                String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat";
                responseString = response_head + responseString;
//这里准备发送给给客户端的数据
                os.write(responseString.getBytes());
                os.flush();
//把字符串转换成字节数组发送出去
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}