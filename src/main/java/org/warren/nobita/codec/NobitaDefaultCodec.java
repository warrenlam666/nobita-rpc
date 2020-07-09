package org.warren.nobita.codec;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class NobitaDefaultCodec implements Codec {

    public byte[] encoder(Object obj){
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Hessian2Output hessian2Output = new Hessian2Output(outputStream);
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            return outputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T decoder(Class<T> clazz, Object src) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream((byte[]) src)
        ) {
            Hessian2Input hessian2Input = new Hessian2Input(inputStream);
            return (T) hessian2Input.readObject(clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
