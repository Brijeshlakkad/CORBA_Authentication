/**
 * Developed to present CORBA usage for Distributes Systems (SOEN 423) course.
 * Developed by Brijesh Lakkad
 *
 * @author brijeshlakkad
 */

import AuthenticationApp.AuthenticationPOA;
import org.omg.CORBA.ORB;

public class AuthenticationImpl extends AuthenticationPOA {
    private ORB orb;

    public void setORB(ORB orb) {
        this.orb = orb;
    }

    @Override
    public boolean login(String username, String password) {
        return username.equalsIgnoreCase("SOEN") && password.equals("423");
    }

    @Override
    public void shutdown() {
        orb.shutdown(true);
    }
}
