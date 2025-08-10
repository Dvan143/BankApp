package org.example.db.User;

import lombok.extern.slf4j.Slf4j;
import org.example.db.User.Exceptions.NotEnoughMoneyException;
import org.example.db.User.Exceptions.NotExistCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Transactional
    public void saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(User user) {
        return user.getUserInfo();
    }

    @Transactional
    public void changeName(User user, String name) {
        user.getUserInfo().setName(name);

        log.debug("User {} changed name to {}", user.getUsername(), name);
    }

    @Transactional
    public void changeBirthday(User user, String date) {
        user.getUserInfo().setBirthdayDate(date);

        log.debug("User {} changed birthday day to {}", user.getUsername(), date);
    }

    @Transactional
    public void changeEmail(User user, String email) {
        user.getUserInfo().setEmail(email);

        log.debug("User {} changed email to {}", user.getUsername(), email);
    }

    @Transactional
    public void setVerificationCode(User user, String code) {
        user.getUserInfo().setEmailVerificationCode(code);

        log.debug("User {} got new verificationCode", user.getUsername());
    }

    @Transactional(readOnly = true)
    public boolean checkVerificationCode(User user, String code) {
        boolean isVerificationCodeEquals = user.getUserInfo().getEmailVerificationCode().equals(code);
        if (isVerificationCodeEquals) {
            log.debug("User {} entered correct verification code", user.getUsername());
        } else {
            log.debug("User {} entered incorrect verification code {}", user.getUsername(), code);
        }
        return isVerificationCodeEquals;
    }

    @Transactional
    public void setEmail(User user, String email) {
        user.getUserInfo().setEmail(email);
        log.debug("User {} set new email {}", user.getUsername(), email);
    }

    @Transactional(readOnly = true)
    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public Map<String, String> isUsernameAndEmailExist(String username, String email){
        List<UserDto> results = userRepository.getByUsernameOrEmail(username,email);
        boolean usernameExists = false;
        boolean emailExists = false;

        for (UserDto result : results) {
            if (username.equals(result.getUsername())) {
                usernameExists = true;
            }
            if (email.equals(result.getEmail())) {
                emailExists = true;
            }
        }

        if (usernameExists && emailExists) {
            return Map.of("status", "bothExist");
        } else if (usernameExists) {
            return Map.of("status", "usernameExist");
        } else if (emailExists) {
            return Map.of("status", "emailExist");
        }

        return Map.of("status", "ok");
    }

    @Transactional
    public void sendMoney(User from, User to, String money, String typeOfAccount) throws NotExistCurrencyException, NotEnoughMoneyException {
        BigDecimal convertedMoney = new BigDecimal(money);
        switch (typeOfAccount) {
            case "usdAccount":
                BigDecimal currentUsdBalanceSender = from.getUserInfo().getUsdAccount();
                BigDecimal currentUsdBalanceRecipient = to.getUserInfo().getUsdAccount();

                boolean isMoneyEnough = currentUsdBalanceSender.compareTo(convertedMoney)>=1;
                if(!isMoneyEnough) throw new NotEnoughMoneyException("Money not enough");

                from.getUserInfo().setUsdAccount(currentUsdBalanceSender.subtract(convertedMoney));
                to.getUserInfo().setUsdAccount(currentUsdBalanceRecipient.add(convertedMoney));
                break;
            case "gbpAccount":
                BigDecimal currentGbpBalanceSender = from.getUserInfo().getGbpAccount();
                BigDecimal currentGbpBalanceRecipient = to.getUserInfo().getGbpAccount();
                from.getUserInfo().setGbpAccount(currentGbpBalanceSender.subtract(convertedMoney));
                to.getUserInfo().setGbpAccount(currentGbpBalanceRecipient.add(convertedMoney));
                break;
            case "jpyAccount":
                BigDecimal currentJpyBalanceSender = from.getUserInfo().getJpyAccount();
                BigDecimal currentJpyBalanceRecipient = to.getUserInfo().getJpyAccount();
                from.getUserInfo().setJpyAccount(currentJpyBalanceSender.subtract(convertedMoney));
                to.getUserInfo().setJpyAccount(currentJpyBalanceRecipient.add(convertedMoney));
                break;
            case "rubAccount":
                BigDecimal currentRubBalanceSender = from.getUserInfo().getRubAccount();
                BigDecimal currentRubBalanceRecipient = to.getUserInfo().getRubAccount();
                from.getUserInfo().setRubAccount(currentRubBalanceSender.subtract(convertedMoney));
                to.getUserInfo().setRubAccount(currentRubBalanceRecipient.add(convertedMoney));
                break;
            default:
                throw new NotExistCurrencyException("Currency not found");
        }
    }
}
