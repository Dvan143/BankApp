package org.example.db.User;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class MetaInfoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String projectName;
    private String senderUsername;
    private String senderIp;
    @Setter
    private String verificationCode;

    public MetaInfoDto() {
    }

    public MetaInfoDto(String projectName, String senderUsername, String senderIp) {
        this.projectName = projectName;
        this.senderUsername = senderUsername;
        this.senderIp = senderIp;
    }
}
