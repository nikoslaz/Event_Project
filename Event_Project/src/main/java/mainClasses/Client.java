package mainClasses;

/**
 *
 * @author nikos, nikoletta, michalis
 */
public class Client {

    private String client_username;
    private String client_password;
    private String client_firstname;
    private String client_lastname;
    private String client_email;
    private int client_phone;
    private int client_balance;
    private int card_number;
    private String card_expdate;
    private int card_cvv;

    public String getClientUsername() {
        return client_username;
    }

    public void setClientUsername(String client_username) {
        this.client_username = client_username;
    }

    public String getClientPassword() {
        return client_password;
    }

    public void setClientPassword(String client_password) {
        this.client_password = client_password;
    }

    public String getClientName() {
        return client_firstname;
    }

    public void setClientName(String client_firstname) {
        this.client_firstname = client_firstname;
    }

    public String getClientLastname() {
        return client_lastname;
    }

    public void setClientLastname(String client_lastname) {
        this.client_lastname = client_lastname;
    }

    public String getClientEmail() {
        return client_email;
    }

    public void setClientEmail(String client_email) {
        this.client_email = client_email;
    }

    public int getClientPhone() {
        return client_phone;
    }

    public void setClientPhone(int client_phone) {
        this.client_phone = client_phone;
    }

    public int getClientBalance() {
        return client_balance;
    }

    public void setClientBalance(int client_balance) {
        this.client_balance = client_balance;
    }

    public int getCardNumber() {
        return card_number;
    }

    public void setCardNumber(int card_number) {
        this.card_number = card_number;
    }

    public String getCardExpDate() {
        return card_expdate;
    }

    public void setCardExpDate(String card_expdate) {
        this.card_expdate = card_expdate;
    }

    public int getCardCvv() {
        return card_cvv;
    }

    public void setCardCvv(int card_cvv) {
        this.card_cvv = card_cvv;
    }
}
