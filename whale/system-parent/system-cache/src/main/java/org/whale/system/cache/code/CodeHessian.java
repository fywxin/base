package org.whale.system.cache.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class CodeHessian<M extends Serializable> implements Code<M> {

	@Override
	public byte[] encode(String cacheName, M m) throws IOException {
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    HessianOutput ho = new HessianOutput(os);  
	    ho.writeObject(m);  
	    return os.toByteArray(); 
	}

	@SuppressWarnings("all")
	@Override
	public M decode(String cacheName, byte[] bytes) throws IOException {
		if(bytes==null) throw new NullPointerException();  
	      
	    ByteArrayInputStream is = new ByteArrayInputStream(bytes);  
	    HessianInput hi = new HessianInput(is);  
	    return (M)hi.readObject(); 
	}

}
