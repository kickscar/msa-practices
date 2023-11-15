package com.poscodx.msa.service.storage.httpd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
@Component
public class SimpleHttpd {
    @Value("${storage.httpd.port}")
    private int port;

    @Value("${storage.location}")
    private String storageLocation;

    public void start() {
       ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind( new InetSocketAddress("0.0.0.0", port));

            log.info("Simple Httpd: starts... [" + port + "]");

            while (true) {
                Socket socket = serverSocket.accept();
                new RequestHandler(socket).start();
            }
        } catch (IOException ex) {
            log.error("Simple Httpd: " + ex.getMessage());
        } finally {
            try {
                if (serverSocket != null && serverSocket.isClosed() == false) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private class RequestHandler extends Thread {
        private Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                OutputStream outputStream = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

                // logging Remote Host IP Address & Port
                InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
                log.info("Simple Httpd: connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort());

                String request = null;
                while(true) {
                    String line = br.readLine();

                    // 브라우저가 연결을 끊으면,
                    if(line == null) {
                        break;
                    }

                    // SimpleHttpServer에서는 요청의 헤더만 읽음
                    if("".equals(line)) {
                        break;
                    }

                    // 헤더의 첫 번째 라인만 읽음
                    if(request == null) {
                        request = line;
                        break;
                    }
                }

                String[] tokens = request.split(" ");
                if("GET".equals(tokens[0])) {
                    reponseStaticResource(outputStream, tokens[1], tokens[2]);
                } else {
                    // methods: POST, PUT, DELETE, HEAD, CONNECT, OPTIONS, ...
                    // Simple Httpd 에서는 무시
                    log.info("Simple Httpd: 400 Bad Request [" + request + "]");
                }
            } catch(Exception ex) {
                log.error("Simple Httpd:" + ex.getMessage());
            } finally {
                try {
                    if(socket != null && socket.isClosed() == false) {
                        socket.close();
                    }
                } catch(IOException ex) {
                }
            }
        }

        private void reponseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {
            String[] locations = storageLocation.split("/");
            String documentRoot = storageLocation.replaceAll("/" + locations[locations.length-1], "");

            File file = new File(documentRoot + url);
            if(!file.exists()) {
                response404Error(outputStream, url, protocol);
                return;
            }

            byte[] body = Files.readAllBytes(file.toPath());
            String contentType = Files.probeContentType(file.toPath());

            outputStream.write((protocol + " 200 OK\n").getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Content-Type:" + contentType + "; charset=utf-8\n").getBytes(StandardCharsets.UTF_8));
            outputStream.write("\n".getBytes() );
            outputStream.write(body);
        }

        private void response404Error(OutputStream os, String url, String protocol)  throws IOException {
            log.info("Simple Httpd: 404 Not Found [" + url + "]");

            os.write((protocol + " 404 File Not Found\n").getBytes("UTF-8"));
            os.write(("Content-Type:text/plain; charset=utf-8\n").getBytes(StandardCharsets.UTF_8));
            os.write("\r\n".getBytes(StandardCharsets.UTF_8));
            os.write("Not Found".getBytes(StandardCharsets.UTF_8));
        }
    }
}
