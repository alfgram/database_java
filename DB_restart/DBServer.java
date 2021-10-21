import DBcommands.DBcmd;
import queryExceptions.queryException;
import java.io.*;
import java.net.*;

class DBServer
{
    parser queryParser = new parser();

    public DBServer(int portNumber)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            ioe.getStackTrace();
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost" + npe.getStackTrace());
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException
    {
        String incomingCommand = socketReader.readLine();
        try {
            DBcmd command = queryParser.parseQuery(incomingCommand);
            command.executeCMD();
        } catch (queryException exception) {
            System.out.println("[ERROR] " + exception);
        }
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
    DBServer server = new DBServer(8888);
    }

}
