/**
 * @author - Nguyen Duy Thai
 */
package model;

import java.time.LocalDate;

public class ResearchStaff extends Staff {
    // attributes
    private int researchPapers;
    private static int standardResearchPapers;

    // default constructor
    public ResearchStaff() {
        setType(TypeStaff.ResearchStaff);
    }

    // parameterized constructor
    public ResearchStaff(String name, String id, LocalDate dob, int researchPapers, String phone, Gender gender) {
        setType(TypeStaff.ResearchStaff);
        setName(name);
        setId(id);
        setDob(dob);
        setPhone(phone);
        setGender(gender);
        this.researchPapers = researchPapers;
    }

    // accessor and mutator methods
    public int getResearchPapers() {
        return researchPapers;
    }

    public void setResearchPapers(int researchPapers) {
        this.researchPapers = researchPapers;
    }

    public static int getStandardResearchPapers() {
        return standardResearchPapers;
    }

    public static void setStandardResearchPapers(int standardResearchPapers) {
        ResearchStaff.standardResearchPapers = standardResearchPapers;
    }

    /**
     * Method isRewarded() check if the research staff is rewarded or not
     *
     * @return true if the number of research papers is 2 more than the number of the standard research papers, false
     * otherwise
     */
    @Override
    public boolean isRewarded() {
        return (researchPapers - standardResearchPapers) > 2;
    }
}
