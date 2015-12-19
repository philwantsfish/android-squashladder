package fish.philwants.glwaterloosquash.data;


public class PlayerProfileInfo {
    private String playerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String group;

    public PlayerProfileInfo(String playerId, String firstName, String lastName, String phoneNumber, String email, String group) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.group = group;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toString() {
        String seperator = ", ";
        StringBuilder builder = new StringBuilder();
        builder.append(playerId);
        builder.append(seperator);
        builder.append(firstName);
        builder.append(seperator);
        builder.append(lastName);
        builder.append(seperator);
        builder.append(email);
        builder.append(seperator);
        builder.append(phoneNumber);
        builder.append(seperator);
        builder.append(group);
        return builder.toString();
    }


}
