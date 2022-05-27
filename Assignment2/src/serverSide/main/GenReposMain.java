package serverSide.main;

import java.net.SocketTimeoutException;

import commInfra.*;
import serverSide.entities.GenReposClientProxy;
import serverSide.sharedRegions.GenReposInterface;
import serverSide.sharedRegions.GenRepos;


/**
 * This class implements the Repository Main that instantiates the shared region Stubs
 * that are part of Repository arguments, instantiates the Repository Interface
 * and launches the Repository Proxy.
 */
public class GenReposMain {

	public static boolean waitConnection;
	
    public static boolean finished;

    public static void main(String[] args) {

        //Repository port
        final int portNumb = ExecConsts.GenReposPort;

        ServerCom scon, sconi;
        GenReposClientProxy repoProxy;

        //Create listening channel
        scon = new ServerCom(portNumb);
        scon.start();

        //Instantiate Shared Region
        GenRepos repos = null;
        repos = new GenRepos(ExecConsts.NUM_PASSANGERS, ExecConsts.fileName);

        //Instantiate Shared Region interface
        GenReposInterface reposInterface = new GenReposInterface(repos);

        //Process Requests while clients not finished
        finished = false;
        while (!finished)
        { 	try {
            //listening
            sconi = scon.accept ();
            //Launch proxy
            repoProxy = new GenReposClientProxy(sconi, reposInterface);
            repoProxy.start ();
        } catch (SocketTimeoutException e) {}
        }
        //Terminate operations
        scon.end();
        repos.reportSummary();
    }

}