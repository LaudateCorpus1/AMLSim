package amlsim;

import amlsim.model.aml.AMLTypology;
import paysim.PaySim;

import java.util.*;

/*
 * Group of fraud transactions and involving accounts
 * Fraud accounts in this class perform suspicious transactions based on a fraud rule
 */
public class Alert {

    private long alertID;  // Alert identifier
    private List<Account> members;  // Accounts involved in this alert
    private Account mainAccount;   // Main account of the fraud alert
    private AMLTypology model;    // Transaction model
    private AMLSim amlsim;  // AMLSim main object

    public Alert(long alertID, AMLTypology model, AMLSim sim){
        this.alertID = alertID;
        this.members = new ArrayList<>();
        this.mainAccount = null;
        this.model = model;
        this.model.setAlert(this);
        this.amlsim = sim;
    }

    /**
     * Add transactions
     * @param step Current simulation step
     */
    public void registerTransactions(long step, Account acct){
        if(model.isValidStep(step)){
            model.sendTransactions(step, acct);
        }
    }

    /**
     * Involve an account in this alert
     * @param acct Account object
     */
    public void addMember(Account acct){
        this.members.add(acct);
        acct.addAlertGroup(this);
    }

    /**
     * Get main AMLSim object
     * @return AMLSim object
     */
    public PaySim getSimulator(){
        return amlsim;
    }

    /**
     * Get alert identifier as long type
     * @return Alert identifier
     */
    public long getAlertID(){
        return alertID;
    }

    /**
     * Get member list of the alert
     * @return Alert account list
     */
    public List<Account> getMembers(){
        return members;
    }

    /**
     * Get the main account
     * @return If this alert is SAR, return the subject account. Otherwise, return null.
     */
    public Account getMainAccount(){
        return mainAccount;
    }

    /**
     * Get the primary account
     * @return If this alert is SAR, return the subject account.
     * If this alert is a false-alert and has one or more members, return the first element of the member list.
     * If this alert has no members, return null.
     */
    Account getPrimaryAccount(){
        if(isSAR()){
            return getMainAccount();
        }else if(members.isEmpty()){
            return null;
        }else{
            return members.get(0);
        }
    }

    /**
     * Set the main account
     * @param fraudster Main account object
     */
    void setMainAccount(SARAccount fraudster){
        this.mainAccount = fraudster;
    }

    public AMLTypology getModel(){
        return model;
    }

    public boolean isSAR(){
        return this.mainAccount != null;  // The alert group is fraud if and only if a subject account exists
    }
}

