/**
 * Developed to present CORBA usage for Distributes Systems (SOEN 423) course.
 * Developed by Brijesh Lakkad
 *
 * @author brijeshlakkad
 */

import AuthenticationApp.Authentication;
import AuthenticationApp.AuthenticationHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Scanner;

public class AuthenticationClient {
    static Authentication authentication;

    public static void main(String args[]) {
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            // get the root naming context
            org.omg.CORBA.Object objRef = orb.string_to_object("corbaloc::localhost:8050/NameService");
            // Use NamingContextExt instead of NamingContext. This is part of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            // resolve the Object Reference in Naming
            String name = "Authentication";
            authentication = AuthenticationHelper.narrow(ncRef.resolve_str(name));
            System.out.println("Obtained a handle on server object: " + authentication);

            Scanner kbd = new Scanner(System.in);

            boolean isLoggedIn = false;
            int maxTries = 3;
            int tryCount = 0;
            while (tryCount < maxTries) {
                System.out.println("Enter username:");
                String username = kbd.next();

                System.out.println("Enter password:");
                String password = kbd.next();
                isLoggedIn = authentication.login(username, password);
                if (isLoggedIn) {
                    break;
                }
                System.out.println("Sorry, try again.");
                tryCount++;
            }
            if (isLoggedIn) {
                System.out.println("You are logged in!!");
            } else {
                System.out.println(maxTries + " incorrect password attempts");
            }
            authentication.shutdown();
        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
}