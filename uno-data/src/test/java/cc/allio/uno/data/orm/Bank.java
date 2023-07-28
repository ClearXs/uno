package cc.allio.uno.data.orm;

import lombok.Data;

@Data
public class Bank {

    private int accountNumber;
    private long balance;
    private String firstname;
    private String lastname;
    private int age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;
}
