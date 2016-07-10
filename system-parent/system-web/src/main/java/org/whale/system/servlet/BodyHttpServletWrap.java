package org.whale.system.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class BodyHttpServletWrap extends HttpServletRequestWrapper {
	
	private final byte[] body;

	public BodyHttpServletWrap(HttpServletRequest request) throws IOException {
		super(request);
		InputStream inputStream = request.getInputStream();
        body = IOUtils.toByteArray(inputStream);
        System.out.println(IOUtils.toString(body, "UTF-8"));
	}

	@Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}
