package org.warren.nobita.codec;

import java.io.IOException;

public interface Codec {

    byte[] encoder(Object obj) throws IOException;

    <T> T decoder(Class<T> clazz, Object src);

}
