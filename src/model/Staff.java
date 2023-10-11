/**
 * @author - Nguyen Duy Thai
 */
package model;

import java.time.LocalDate;

public abstract class Staff {
    // attributes
    private String name;
    private String id;
    private LocalDate dob;
    private String phone;
    private Gender gender;
    private TypeStaff type;

    // accessor and mutator methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public TypeStaff getType() {
        return type;
    }

    protected void setType(TypeStaff type) {
        this.type = type;
    }

    /**
     * Method isRewarded() check if the staff is rewarded or not
     *
     * @return true if the staff is rewarded, false otherwise
     */
    public abstract boolean isRewarded();
}
