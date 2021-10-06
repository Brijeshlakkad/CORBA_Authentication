/**
 * Developed to present CORBA usage for Distributes Systems (SOEN 423) course.
 * Developed by Brijesh Lakkad
 *
 * @author brijeshlakkad
 */

import AuthenticationApp.Authentication;
import AuthenticationApp.AuthenticationHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class AuthenticationServer {
    public static void main(String args[]) {
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            props.put("org.omg.CORBA.ORBInitialPort", "8050");
            String[] newArgs = new String[0];
            // create and initialize the ORB
            ORB orb = ORB.init(newArgs, props);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            AuthenticationImpl additionImpl = new AuthenticationImpl();
            additionImpl.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(additionImpl);
            Authentication href = AuthenticationHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef = orb.string_to_object("corbaloc::localhost:8050/NameService"); // InvalidName
            // Use NamingContextExt which is part of the Interoperable Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            // bind the Object Reference in Naming
            String name = "Authentication";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);
            System.out.println("Authentication ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
        System.out.println("HelloServer Exiting ...");
    }
}
