package com.park.mall.service.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestBody {

    @JsonProperty("application_id")
    private String applicationId;

    @JsonProperty("private_key")
    private String privateKey;
}
