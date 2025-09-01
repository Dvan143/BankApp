package org.example.db.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
public class UserInfo {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "userinfo_seq", sequenceName = "userinfo_sequence")
    private Long id;
    @JsonIgnore
    @OneToOne(mappedBy = "userInfo")
    private User user;
    @Setter
    private String name;
    @Setter
    private String email;
    @JsonIgnore
    @Setter
    private String emailVerificationCode;
    @Setter
    private String birthdayDate;
    @Setter
    private BigDecimal usdAccount;
    @Setter
    private BigDecimal gbpAccount;
    @Setter
    private BigDecimal jpyAccount;
    @Setter
    private BigDecimal rubAccount;

    public UserInfo() {
        usdAccount = BigDecimal.valueOf(10000);
        gbpAccount = BigDecimal.valueOf(10000);
        jpyAccount = BigDecimal.valueOf(10000);
        rubAccount = BigDecimal.valueOf(10000);
    }
}
