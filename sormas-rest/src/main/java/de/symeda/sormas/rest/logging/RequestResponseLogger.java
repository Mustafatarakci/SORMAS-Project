package de.symeda.sormas.rest.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation class RequestLogger
 */
@WebFilter("/*")
public class RequestResponseLogger implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Default constructor.
	 */
	public RequestResponseLogger() {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// request logging
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.debug("requestUri = {}", httpRequest.getRequestURI());

		Map<String, String[]> params = httpRequest.getParameterMap();
		for (String s : params.keySet()) {
			logger.debug("  {} = {}", s, httpRequest.getParameter(s));
		}

		if (response.getCharacterEncoding() == null) {
			response.setCharacterEncoding("UTF-8");
		}

		if (logger.isTraceEnabled()) {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			LoggingHttpServletResponseWrapper wrapper = new LoggingHttpServletResponseWrapper(httpServletResponse);
			try {
				// pass the request along the filter chain
				chain.doFilter(request, wrapper);
				httpServletResponse.getOutputStream().write(wrapper.getContentAsBytes());
			} finally {
				logger.trace(wrapper.getContent());
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

	public class LoggingHttpServletResponseWrapper extends HttpServletResponseWrapper {

		private final LoggingServletOutputStream loggingServletOutputStream = new LoggingServletOutputStream();

		private final HttpServletResponse delegate;

		public LoggingHttpServletResponseWrapper(HttpServletResponse response) {
			super(response);
			delegate = response;
		}

		@Override
		public ServletOutputStream getOutputStream() {
			return loggingServletOutputStream;
		}

		@Override
		public PrintWriter getWriter() {
			return new PrintWriter(loggingServletOutputStream.baos);
		}

		public Map<String, String> getHeaders() {
			Map<String, String> headers = new HashMap<>(0);
			for (String headerName : getHeaderNames()) {
				headers.put(headerName, getHeader(headerName));
			}
			return headers;
		}

		public String getContent() {
			try {
				return loggingServletOutputStream.baos.toString(delegate.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				return "[UNSUPPORTED ENCODING]";
			}
		}

		public byte[] getContentAsBytes() {
			return loggingServletOutputStream.baos.toByteArray();
		}

		private class LoggingServletOutputStream extends ServletOutputStream {

			private ByteArrayOutputStream baos = new ByteArrayOutputStream();

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {
				// not used
			}

			@Override
			public void write(int b) {
				baos.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				baos.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) {
				baos.write(b, off, len);
			}
		}
	}
}
