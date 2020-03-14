package me.stqlth.solent.messages.debug;

import java.sql.SQLException;

public class DebugMessages {

    public void sqlDebug(SQLException ex) {
        System.out.println("SQLExpection: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

}


