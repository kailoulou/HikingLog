package com.example.demo;

import java.util.ArrayList;

public class PlanList {
    private ArrayList<Plan> planList;

    /** Constructor initialises the empty trip list
     */
    public PlanList() {
        planList = new ArrayList<>();
    }

    /** Adds a new plan to the list
     *  @param  planIn: The plan to add
     */
    public void addPlan(Plan planIn) {
        planList.add(planIn);
    }

    /** Removes the item
     *  @param planIn The plan to remove
     */
    public void removePlan(Plan planIn) {
        Plan find = search(planIn.getLocation()); // call search method
        planList.remove(find); // remove plan

    }

    /** Gets the total number of plans
     *  @return Returns the total number of plans currently in the list
     */
    public int getTotal() {
        return planList.size();
    }

    /** clears the list
     */
    public void clearList() {
        planList.clear();
    }

    /** Searches for the plan in the list
     *  @param nameIn The plan to search for
     *  @return Returns the plan or null if it is not in the list
     */
    public Plan search(String nameIn) {
        for(Plan currentPlan: planList) {
            // find the plan with the given location
            // case-insensitive, spelling sensitive
            if(currentPlan.getLocation().equalsIgnoreCase(nameIn)) {
                return currentPlan;
            }
        }
        return null; // no plan found in the list
    }

    /** Reads the plan at the given position in the list
     *  @param      positionIn The position of the plan in the list
     *  @return     Returns the plan at the given logical position in the list
     *              or null if no plan at that logical position
     */
    public Plan getPlan(int positionIn)
    {
        if (positionIn<0 || positionIn>=getTotal()) {// check for valid position
            return null; // no object found at given position
        }
        else {
            // remove one frm logical poition to get ArrayList position
            return planList.get(positionIn);
        }
    }

    /** Reads the plan at the given position in the list
     *  @param      planIn the plan at the given logical position in the list
     *  @return     Returns the logical position of the plan in the list
     *              or null if no plan in the list
     */
    public int getIndex(String planIn)
    {
        for(int i = 0; i < planList.size(); i++){
            if (planList.get(i).getLocation().equalsIgnoreCase(planIn)){
                return i;
            }
        }
        return -1;
    }

    /** Gets the plan list
     *  @return Returns array list of plans
     */
    public ArrayList<Plan> getList() {
        return planList;
    }


    @Override
    public String toString() {
        return planList.toString();
    }
}
