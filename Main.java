package denis.knopers;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws ProtocolException, FileNotFoundException, MalformedURLException {
        String urlParameters  = ""; //data 
        byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = ""; // URL here
        URL url            = new URL( request );
        int i = 0;
        
        // create Scanner inFile1
        Scanner inFile1 = new Scanner(new File("proxy.txt"));

        List<String> temps = new ArrayList<String>();

        while (inFile1.hasNext()) {
            // find next line
            String proxy = inFile1.next();
            temps.add(proxy);
        }
        inFile1.close();
        
        for(String proxy : temps) {
            String[] p = proxy.split(":");
            String ip = p[0];
            int port = Integer.parseInt(p[1]);
            System.out.print(ip + ":" +port + "\n");
            Proxy proxys = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
            HttpURLConnection conn= null;
            try {
                conn = (HttpURLConnection) url.openConnection(proxys);
            } catch (IOException e) {
                System.out.print("Nie otworzono połączenias \n");
            }
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" ); //request method
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            conn.setConnectTimeout(10000);
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
                //System.out.print(conn.getOutputStream().toString());
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.print("Time outs \n");
            }
            Reader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                for (int c; (c = in.read()) >= 0;) {
                    System.out.print((char) c);
                    if ((char) c == 't')
                        i++;
                    System.out.print(i);
                }
            } catch (IOException e) {
                System.out.print("Proxy dont work \n");
            }

        }

        }
}
