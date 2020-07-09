package org.warren.nobita.protocol;

import lombok.*;

import java.io.Serializable;
import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NobitaRequest  implements Serializable {

    private String requestId;

    private String interfaceName;

    private String methodName;

    private String[] parametersClassName;

    private Object[] parametersValue;

}
