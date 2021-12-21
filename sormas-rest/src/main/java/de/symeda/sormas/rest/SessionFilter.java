package de.symeda.sormas.rest;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class SessionFilter implements Filter {

	@EJB
	private SessionFilterBean sessionFilterBean;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		sessionFilterBean.doFilter(chain, request, response);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {

	}
}
