package org.example.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDto {
    private String from;
    private String body;
    private String additionalInfo;

    public LogDto() {
    }

    public LogDto(String from, String body, String additionalInfo) {
        this.from = from;
        this.body = body;
        this.additionalInfo = additionalInfo;
    }
}
