/**
 * @author - Nguyen Duy Thai
 */
package model;

import java.time.LocalDate;

public class ServiceStaff extends Staff {
    // attributes
    private double serviceHours;
    private static double standardServiceHours;

    // default constructor
    public ServiceStaff(){
        setType(TypeStaff.ServiceStaff);
    }

    // parameterized constructor
    public ServiceStaff(String name, String id, LocalDate dob, double serviceHours, String phone, Gender gender){
        setType(TypeStaff.ServiceStaff);
        setName(name);
        setId(id);
        setDob(dob);
        setPhone(phone);
        setGender(gender);
        this.serviceHours = serviceHours;
    }

    // accessor and mutator methods
    public double getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(double serviceHours) {
        this.serviceHours = serviceHours;
    }

    public static double getStandardServiceHours() {
        return standardServiceHours;
    }

    public static void setStandardServiceHours(double standardServiceHours) {
        ServiceStaff.standardServiceHours = standardServiceHours;
    }

    /**
     * Method isRewarded() check if the service staff is rewarded or not
     *
     * @return true if the service hours is greater than 150% of the standard service hours, false otherwise
     */
    @Override
    public boolean isRewarded() {
        return serviceHours > standardServiceHours * 1.5;
    }
}
