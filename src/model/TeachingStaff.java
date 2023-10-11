/**
 * @author - Nguyen Duy Thai
 */
package model;

import java.time.LocalDate;

public class TeachingStaff extends Staff {
    // attributes
    private double teachingHours;
    private static double standardTeachingHours;

    // default constructor
    public TeachingStaff(){
        setType(TypeStaff.TeachingStaff);
    }

    // parameterized constructor
    public TeachingStaff(String name, String id, LocalDate dob, double teachingHours, String phone, Gender gender){
        setType(TypeStaff.TeachingStaff);
        setName(name);
        setId(id);
        setDob(dob);
        setGender(gender);
        setPhone(phone);
        this.teachingHours = teachingHours;
    }

    // accessor and mutator methods
    public double getTeachingHours() {
        return teachingHours;
    }

    public void setTeachingHours(double teachingHours) {
        this.teachingHours = teachingHours;
    }

    public static double getStandardTeachingHours() {
        return standardTeachingHours;
    }

    public static void setStandardTeachingHours(double standardTeachingHours) {
        TeachingStaff.standardTeachingHours = standardTeachingHours;
    }

    /**
     * Method isRewarded() check if the teaching staff is rewarded or not
     *
     * @return true if the teaching hours is greater than 120% of the standard teaching hours, false otherwise
     */
    @Override
    public boolean isRewarded() {
        return teachingHours > standardTeachingHours * 1.2;
    }
}
