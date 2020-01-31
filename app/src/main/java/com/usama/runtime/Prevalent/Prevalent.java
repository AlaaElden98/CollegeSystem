package com.usama.runtime.Prevalent;

import com.usama.runtime.model.Doctors;
import com.usama.runtime.model.Student;

public class Prevalent {
    // student side to save current info
    public static Student CurrentOnlineStudent;
    public static final String StudentNationalIdKey = "StudentNationalId";
    public static final String StudentSittingNumberKey = "StudentSittingNumber";


    // admin side to save current info
    public static Doctors CurrentOnlineAdminOrDoctor;

    public static final String DoctorOrAdminNationalIDKey = "DoctorOrAdminNationalID";
    public static final String DoctorOrAdminPasswordKey = "DoctorOrAdminPassword";
}
