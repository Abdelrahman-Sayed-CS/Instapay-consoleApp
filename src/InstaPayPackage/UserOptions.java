package InstaPayPackage;

import java.sql.SQLException;

public class UserOptions {
  private DatabaseHandler DB;
  private InstapayAccount myAccount;
  private Bill bill;

  public UserOptions(InstapayAccount account) {
    this.myAccount = account;
    try {
      DB = new DatabaseHandler();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  InstapayAccount getMyAccount() {
    return myAccount;
  }

  public Bill getBill() {
    return this.bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  public boolean TransferToWallet(String mobileNumber, double money) {
    return DB.updateBalance("Wallet", money, mobileNumber) &&
            DB.updateBalance(myAccount.getType(), -money, myAccount.getNumberPhone());
  }

  public boolean TransferToBankAcc(String mobileNumber, double money) {
    return DB.updateBalance("BankAccount", money, mobileNumber) &&
            DB.updateBalance(myAccount.getType(), -money, myAccount.getNumberPhone());
  }

  public boolean AbilityToTransfer(double money) throws SQLException {
    return (getAccountBalance(myAccount) >= money) && money > 0;
  }

  public void payBill(InstapayAccount account, double cost) {
    DB.updateBalance(account.getType(), cost, account.getNumberPhone());
  }

  public boolean Transfer(String userName, double money) {
    InstapayAccount recipientAccount = DB.getInstapayAcc(userName);
    if (recipientAccount == null) {
      System.out.println("This userName is not Exist");
      return false;
    }

    String accountType = recipientAccount.getType();
    if (accountType.equals("Wallet")) {
      return TransferToWallet(recipientAccount.getNumberPhone(), money);
    } else if (accountType.equals("BankAccount") &&
            myAccount.getType().equals("BankAccount")) {
      return TransferToBankAcc(recipientAccount.getNumberPhone(), money);
    } else {
      System.out.println("You're trying to transfer money from your wallet to a bank account, which is not allowed.");
    }
    return false;
  }

  public double getAccountBalance(InstapayAccount account) throws SQLException {
    return DB.retrieveBalance(account);
  }
  public boolean checkTypeAccountWithMobileNumber(String mobile, String tableN){
    return DB.checkTypeUsingNumber(mobile, tableN);
  }
//  chcek if this username is exist or no
  public boolean checkExistenceUser(String username) throws SQLException{
    return DB.userNameIsRegistered(username);
  }
////  check if this number phone is exist or no
//  public  boolean numberPhoneIsExist(String numberPhone) throws SQLException {
//    return DB.numPhoneIsRegistered(numberPhone);
//  }
//  public String getTypeAccount(String mobileNum){
//    return DB.getType(mobileNum);
//  }
}