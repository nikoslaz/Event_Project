package mainClasses;

/**
 *
 * @author nikos
 */
public class Client {

    private String client_username;
    private String client_password;
    private String client_name;
    private String client_lastname;
    private String client_email;
    private int client_phone;
    private int client_cancel_balance;

    // Field for nested CardDetails
    private CardDetails client_card;

    // Nested CardDetails class
    public static class CardDetails {

        private String cardNumber;
        private String cardExpDate;
        private String cardCvv;

        // Getters and Setters for CardDetails
        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardExpDate() {
            return cardExpDate;
        }

        public void setCardExpDate(String cardExpDate) {
            this.cardExpDate = cardExpDate;
        }

        public String getCardCvv() {
            return cardCvv;
        }

        public void setCardCvv(String cardCvv) {
            this.cardCvv = cardCvv;
        }
    }

    // Getters and Setters for Client fields
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
        return client_name;
    }

    public void setClientName(String client_name) {
        this.client_name = client_name;
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
        return client_cancel_balance;
    }

    public void setClientBalance(int client_cancel_balance) {
        this.client_cancel_balance = client_cancel_balance;
    }

    // Getter and Setter for CardDetails
    public CardDetails getClientCard() {
        return client_card;
    }

    public void setClientCard(CardDetails client_card) {
        this.client_card = client_card;
    }

}
