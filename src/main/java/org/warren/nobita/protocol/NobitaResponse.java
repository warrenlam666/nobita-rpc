package org.warren.nobita.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NobitaResponse implements Serializable {

    private String requestId;

    private boolean isSuccess;

    private Object res;

    private Exception exception;

    private boolean timeout;

}
